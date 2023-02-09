// package boomitestbed

class ScriptRunner {
    // private MockableService fileService
    // private MockableService evalService

    // ScriptRunner(MockableService fileService, MockableService evalService) {
        // this.fileService = fileService
        // this.evalService = evalService
    // }

    String run(String scriptName, String dataDocumentName, String propertiesFileName, String outFileExtension, Boolean suppressData, Boolean suppressProps, String ddpPreplacePattern) {

        String script
        def commentDataGroup
        def commentPropsGroup
        def commentOpts
        try {
            script = new FileInputStream(scriptName).text
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
                // println dataDocumentName
                documentContents = new FileInputStream(dataDocumentName)
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
                    documentContents = new FileInputStream(fileName)
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
                properties.load(new FileInputStream(propertiesFileName) as InputStream)
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
                    ArrayList propsFileNameArr = propertiesFileName.split("/")
                    def propsFilePath = propsFileNameArr[0..-2].join("/")
                    String fileName = "${propsFilePath}/${propFileGroup[0][1]}"
                    value = new FileInputStream(fileName).text
                        .replaceAll("\r?\n","")
                    // println value
                    properties.setProperty(key,value)
                }
                else {
                    value = value.replaceAll("\\\\","\\\\\\\\")
                }

                if (!key.startsWith("document.dynamic.userdefined")) {
                    dynamicProcessProperties.setProperty(key, value)
                    properties.remove(key)
                }
            }
        }
        catch (Exception e){
            return "ERROR: Cannot set DPPs: ${e.message}"
        }

        DataContext dataContext = new DataContext(documentContents, properties)
        try {
            ExecutionUtilHelper ExecutionUtil = new ExecutionUtilHelper()
            ExecutionUtil.dynamicProcessProperties = dynamicProcessProperties;
            script = script  -~ /import com[.]boomi[.]execution[.]ExecutionUtil;?/
            // evalService.eval(dataContext, ExecutionUtil, script)

            Eval.xy(dataContext, ExecutionUtil, "def dataContext = x; def ExecutionUtil = y;" +
                    "${script}; return dataContext")

        } catch (Exception e) {
            return "ERROR: ${e.message}"
        }

        def resultString = dataContext.printPretty()
        def dynamicProcessPropsString = prettyProps(dynamicProcessProperties)
        def dynamicDocumentPropsString = prettyProps(properties)

        def output = ""
        // if (script =~ /^\s*[^\/\/]\s*println/) output += "\n"
        // if (!opts.contains("nothing")) {
        //     def numResultItems = 0
        //     if (!opts.contains("noprops")) {
        //         if (dynamicProcessProperties.propertyNames().hasMoreElements()) {
        //             output += "# --- DPPs --- #\n$dynamicProcessPropsString\n"
        //             numResultItems++
        //         }
        //         if (properties.propertyNames().hasMoreElements()) {
        //             output += "# --- DDPs --- #\n$dynamicDocumentPropsString\n"
        //             numResultItems++
        //         }
        //     }
        //     if (!opts.contains("noresult")) {
        //         output += "${numResultItems ? "--------------------------\n\n" : ""}$resultString"
        //     }
        // }
        if (!suppressData) output += resultString

        if (!suppressProps) {
            if (properties.propertyNames().hasMoreElements()) {
                output += "\nDynamic Document Props\n----------------------\n"
                output += dynamicDocumentPropsString
                    .replaceAll("document.dynamic.userdefined.","")
                    .replaceAll(/$ddpPreplacePattern\n/,"")
            }
            if (dynamicProcessProperties.propertyNames().hasMoreElements()) {
                output += "\n\nDYNAMIC PROCESS PROPS\n---------------------\n"
                output += dynamicProcessPropsString
            }
        }

        // println "scriptName: " + scriptName
        ArrayList scriptNameArr = scriptName.split("/")
        // println "scriptNameArr: " + scriptNameArr.toString()
        def scriptPath = scriptNameArr[0..-2].join("/")
        // println "scriptPath: " + scriptPath
        def scriptNameHead = scriptNameArr[-1] -~ /\.b\.groovy$/ -~ /\.groovy$/ 
        // println "scriptNameHead: " + scriptNameHead

        def execFilesPath = "/_exec/"
        File executionFilesDir = new File(scriptPath + execFilesPath);
        if (! executionFilesDir.exists()) executionFilesDir.mkdir()

        File outDataFile = new File(scriptPath + execFilesPath + scriptNameHead + "_out." + outFileExtension)
        outDataFile.write resultString

        // if (propertiesFileName) {
        File outPropsFile = new File(scriptPath + execFilesPath + scriptNameHead + "_out.properties")
        outPropsFile.write dynamicProcessPropsString + "\n" + dynamicDocumentPropsString
        // }

        return output
    }

    private String prettyProps (Properties properties) {
        String propsString = ""
        Enumeration<?> e = properties.propertyNames()
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement()
            String value = properties.getProperty(key)
                                .replaceAll("\r?\n","")
                                .replaceAll("\\\\","\\\\\\\\")
            propsString += "${key}=${value}\n"
        }
        return propsString
    }

}
