
class ScriptRunner {
    private DataContext dataContext = new DataContext()
    String run(Closure closeOver, String dataDocument){
        dataContext.is = new FileInputStream(dataDocument)
        dataContext.props = new Properties()

        closeOver(dataContext)

        "Resulting Document\n" +
        "=============================\n" +
        "${dataContext.is.text}\n" +
        "=============================\n" +
        "Resulting Props${dataContext.props}"
    }
}
