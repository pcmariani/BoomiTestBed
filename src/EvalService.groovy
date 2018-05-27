class EvalService implements MockableService {
    public void eval(DataContext dataContext, String script) {
        Eval.x(dataContext, "def dataContext = x; ${script} ; return dataContext")
    }
}
