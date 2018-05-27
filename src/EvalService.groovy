class EvalService implements MockableService {
    void eval(DataContext dataContext, String script) {
        Eval.x(dataContext, "def dataContext = x; ${script} ; return dataContext")
    }
}
