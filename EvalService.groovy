// package boomitestbed

class EvalService implements MockableService {
    void eval(DataContext dataContext, ExecutionUtilHelper ExecutionUtil, String script) {
        Eval.xy(dataContext, ExecutionUtil, "def dataContext = x; def ExecutionUtil = y;" +
        "${script}; return dataContext")
    }
}
