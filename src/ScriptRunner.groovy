class ScriptRunner {

    String run(String scriptName, String dataDocument) {
        FileInputStream fileInputStream
        try {
            fileInputStream = new FileInputStream(dataDocument)
        } catch (Exception e) {
            return "I can't find the document ${dataDocument}"
        }

        DataContext dataContext = new DataContext(fileInputStream, new Properties())
        String script
        try {
            script = new File(scriptName).text
        } catch (Exception e) {
            return "I can't find the script ${scriptName}"
        }
        try {
            Eval.x(dataContext, "def dataContext = x; ${script} ; return dataContext")
        } catch (Exception e) {
            return "That script does not make sense to me:\n ${e.message}"
        }
        "Resulting Document\n" +
                "=============================\n" +
                "${dataContext.is.text}\n" +
                "=============================\n" +
                "Resulting Props${dataContext.props}"
    }
}
