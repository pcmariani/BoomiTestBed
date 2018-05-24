
class ScriptRunner {

    String run(Closure closeOver, String dataDocument){
        DataContext dataContext = new DataContext(new FileInputStream(dataDocument), new Properties())
        closeOver(dataContext)

        "Resulting Document\n" +
        "=============================\n" +
        "${dataContext.is.text}\n" +
        "=============================\n" +
        "Resulting Props${dataContext.props}"
    }
}
