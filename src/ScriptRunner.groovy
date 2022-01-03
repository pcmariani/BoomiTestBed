package boomitestbed

class ScriptRunner {
    private MockableService fileService
    private MockableService evalService

    ScriptRunner(MockableService fileService, MockableService evalService) {
        this.fileService = fileService
        this.evalService = evalService
    }

    String run(String scriptName, String dataDocumentName, String propertiesFileName) {

        String script
        def commentDataGroup
        def commentPropsGroup
        def commentOpts
        try {
            script = fileService.open(scriptName).text
            commentDataGroup = (script =~ /(?si)\/\*.*?@data.*?(.*?)\s*?[-+=*#^~]*?\*\//)
            commentPropsGroup = (script =~ /(?si)\/\*.*?@props.*?(.*?)\s*?[-+=*#^~]*?\*\//)
            commentOpts = (script =~ /(?i)\/\/.*?@.*?\s*(.*?)\r?\n/)
        } catch (Exception ignored) {
            return "I can't find the script ${scriptName}"
        }

        def opts = []
        if (commentOpts.size()) {
            try {
                opts = commentOpts[commentOpts.size()-1 as int][1].split(/ +|-/)
            } catch (Exception optsEx) {
                return "I can't understand your options ${opts}"
            }
        }

        InputStream documentContents = new ByteArrayInputStream("".getBytes("UTF-8"))
        if (dataDocumentName != null) {
            try {
                documentContents = fileService.open(dataDocumentName)
            } catch (Exception ignored) {
                return "I can't find the document ${dataDocumentName}"
            }
        }
        else if (commentDataGroup.size()) {
            try {
                def dataString = commentDataGroup[0][1] -~ /^.*?\r?\n/

                def propFileGroup = (dataString =~ /(?i)@file\(["'](.*?)["']\)/)
                if (propFileGroup.size() > 0) {
                    String fileName = "${propFileGroup[0][1]}"
                    documentContents = fileService.open(fileName)
                }
                else {
                    documentContents = new ByteArrayInputStream(dataString.getBytes("UTF-8"))
                }
            } catch (Exception e) {
                return "I can't read the data in your @data comment: ${e.message}"
            }
        }

        Properties properties = new Properties()
        if (propertiesFileName != null) {
            try {
                properties.load(fileService.open(propertiesFileName) as InputStream)
            } catch (Exception ignored) {
                return "I can't find the properties ${propertiesFileName}"
            }
        }
        else if (commentPropsGroup.size()) {
            try {
                def propsString = commentPropsGroup[0][1].replaceAll(/(?m)\s+$/,"") -~ /^.*?\r?\n/
                properties.load(new ByteArrayInputStream(propsString.getBytes("UTF-8")))
            } catch (Exception e) {
                return "I can't parse the props in you @props comment: ${e.message}"
            }
        }

        Properties dynamicProcessProperties = new Properties()
        try {
            Enumeration<?> e = properties.propertyNames()
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement()
                String value = properties.getProperty(key)

                def propFileGroup = (value =~ /(?i)^@file\(["'](.*?)["']\)/)
                if (propFileGroup.size() > 0) {
                    String fileName = "${workingDir}\\${propFileGroup[0][1]}"
                    value = fileService.open(fileName).text
                    properties.setProperty(key,value)
                }

                if (!key.startsWith("document.dynamic.userdefined")) {
                    dynamicProcessProperties.setProperty(key, value)
                    properties.remove(key)
                }
            }
        }
        catch (Exception e){
            return "I'm having trouble setting your DPPs: ${e.message}"
        }

        DataContext dataContext = new DataContext(documentContents, properties)
        try {
            ExecutionUtilHelper ExecutionUtil = new ExecutionUtilHelper()
            ExecutionUtil.dynamicProcessProperties = dynamicProcessProperties;
            script = script  -~ /import com[.]boomi[.]execution[.]ExecutionUtil;?/
            evalService.eval(dataContext, ExecutionUtil, script)

        } catch (Exception e) {
            return "That script does not make sense to me: ${e.message}"
        }

        def output = ""
        if (script =~ /^\s*[^\/\/]\s*println/) output += "\n"
        if (!opts.contains("nothing")) {
            def numResultItems = 0
            if (!opts.contains("noprops")) {
                if (dynamicProcessProperties.propertyNames().hasMoreElements()) {
                    output += "# --- DPPs --- #\n${prettyProps(dynamicProcessProperties)}\n"
                    numResultItems++
                }
                if (properties.propertyNames().hasMoreElements()) {
                    output += "# --- DDPs --- #\n${prettyProps(properties)}\n"
                    numResultItems++
                }
            }
            if (!opts.contains("noresult")) {
                output += "${numResultItems ? "# --- Result --- #\n" : ""} ${dataContext.printPretty()}"
            }
        }
        return output
    }

    private String prettyProps (Properties properties) {
        String propsString = ""
        Enumeration<?> e = properties.propertyNames()
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement()
            String value = properties.getProperty(key)
            propsString += "${key} = ${value}\n"
        }
        return propsString
    }

}
