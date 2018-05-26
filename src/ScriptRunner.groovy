class ScriptRunner {

    String run(String scriptName, String dataDocumentName, String propertiesFileName) {
        InputStream fileInputStream = new ByteArrayInputStream("".getBytes("UTF-8"))
        if (dataDocumentName != null) {
            try {
                fileInputStream = new FileInputStream (dataDocumentName)
            } catch (Exception e) {
                return "I can't find the document ${dataDocumentName}"
            }
        }

        String script
        try {
            script = new File(scriptName).text
        } catch (Exception e) {
            return "I can't find the script ${scriptName}"
        }

        Properties properties = new Properties()
        if (propertiesFileName != null) {
            try {
                properties.load(new FileInputStream(propertiesFileName))
            } catch (Exception e) {
                return "I can't find the properties file ${propertiesFileName}"
            }
        }

        DataContext dataContext = new DataContext(fileInputStream, properties)

        try {
            Eval.x(dataContext, "def dataContext = x; ${script} ; return dataContext")
        } catch (Exception e) {
            return "That script does not make sense to me:\n ${e.message}"
        }

        "Resulting Document:\n" +
        "${dataContext.is.text}\n" +
        "Resulting Props${dataContext.props}"
    }
}
