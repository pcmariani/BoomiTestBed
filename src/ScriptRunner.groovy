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
            script = fileService.open(scriptName).text
        } catch (Exception e) {
            return "I can't find the script ${scriptName}"
        }

        InputStream documentContents = new ByteArrayInputStream("".getBytes("UTF-8"))
        if (dataDocumentName != null) {
            try {
                documentContents = fileService.open(dataDocumentName)
            } catch (Exception e) {
                return "I can't find the document ${dataDocumentName}"
            }
        }

        Properties properties = new Properties()
        if (propertiesFileName != null) {
            try {
                properties.load(fileService.open(propertiesFileName))
            } catch (Exception e) {
                return "I can't find the properties ${propertiesFileName}"
            }
        }

        DataContext dataContext = new DataContext(documentContents, properties)
        try {
            evalService.eval(dataContext, script)

        } catch (Exception e) {
            return "That script does not make sense to me:\n ${e.message}"
        }


        "Resulting Document\n" +
                dataContext.is.text +
                "\n" +
                "Resulting Props\n" +
                properties
    }
}
