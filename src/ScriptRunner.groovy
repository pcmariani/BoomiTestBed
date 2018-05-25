
class ScriptRunner {

    String run(String scriptName, String dataDocument){
        DataContext dataContext = new DataContext(new FileInputStream(dataDocument), new Properties())

        String script = new File(scriptName).text
        try {
            Eval.x(dataContext, "def dataContext = x; ${script} ; return dataContext")
        }
        catch(Exception e){
            return "That script sucked: ${e.message}"
        }
        "Resulting Document\n" +
        "=============================\n" +
        "${dataContext.is.text}\n" +
        "=============================\n" +
        "Resulting Props${dataContext.props}"
    }
}
