class ScriptRunner {
    private MockableService fileService
    private MockableService evalService

    ScriptRunner(MockableService fileService, MockableService evalService) {
        this.fileService = fileService
        this.evalService = evalService
    }

    String run(String scriptName, String dataDocumentName, String propertiesFileName) {

        String script
        try {
            script = fileService.open(scriptName).text -~ /import com[.]boomi[.]execution[.]ExecutionUtil;?/
            // script = fileService.open(scriptName).text
        } catch (Exception ignored) {
            return "I can't find the script ${scriptName}"
        }

        InputStream documentContents = new ByteArrayInputStream("".getBytes("UTF-8"))
        if (dataDocumentName != null) {
            try {
                documentContents = fileService.open(dataDocumentName)
            } catch (Exception ignored) {
                return "I can't find the document ${dataDocumentName}"
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
            return "I'm having trouble setting your DPPs:\n ${e.message}"
        }

        DataContext dataContext = new DataContext(documentContents, properties)
        try {
            ExecutionUtil eu = new ExecutionUtil()
            evalService.eval(dataContext, dynamicProcessProperties, script)
            // evalService.eval(dataContext, script)

        } catch (Exception e) {
            return "That script does not make sense to me:\n ${e.message}"
        }

        def result = ""
        result += "# Dynamic Document Props\n${prettyProps(properties)}\n\n"
        result += "# Dynamic Process Props\n${prettyProps(dynamicProcessProperties)}\n\n"
        result += "Resulting Document\n$dataContext.printPretty()"
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
