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
        try {
            script = fileService.open(scriptName).text -~ /import com[.]boomi[.]execution[.]ExecutionUtil;?/
            commentDataGroup = (script =~ /(?si)\/\*.*?@data.*?(.*?)\s*?[-+=*#^~]*?\*\//)
            commentPropsGroup = (script =~ /(?si)\/\*.*?@props.*?(.*?)\s*?[-+=*#^~]*?\*\//)
        } catch (Exception ignored) {
            return "I can't find the script ${scriptName}"
        }

        def hasData = false
        InputStream documentContents = new ByteArrayInputStream("".getBytes("UTF-8"))
        if (dataDocumentName != null) {
            try {
                documentContents = fileService.open(dataDocumentName)
                hasData = true
            } catch (Exception ignored) {
                return "I can't find the document ${dataDocumentName}"
            }
        }
        else if (commentDataGroup.size()) {
            try {
                def dataString = commentDataGroup[0][1] -~ /^.*?\r?\n/
                documentContents = new ByteArrayInputStream(dataString.getBytes("UTF-8"))
                hasData = true
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
            ExecutionUtil eu = new ExecutionUtil()
            evalService.eval(dataContext, dynamicProcessProperties, script)
            // evalService.eval(dataContext, script)

        } catch (Exception e) {
            return "That script does not make sense to me: ${e.message}"
        }

        def result = ""
        if (dynamicProcessProperties.propertyNames().hasMoreElements()) {
            result += "# --- DPPs --- #\n${prettyProps(dynamicProcessProperties)}\n"
        }
        if (properties.propertyNames().hasMoreElements()) {
            result += "# --- DDPs --- #\n${prettyProps(properties)}\n"
        }
        if (hasData) {
            result += "# --- Result --- #\n${dataContext.printPretty()}"
        }
        return result
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
