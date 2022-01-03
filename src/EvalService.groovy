// import ExecutionUtil
class EvalService implements MockableService {
    void eval(DataContext dataContext, ExecutionUtilHelper ExecutionUtil, String script) {
        Eval.xy(dataContext, ExecutionUtil, "def dataContext = x; def ExecutionUtil = y;" +
        "println 'p2';" +
        // "ExecutionUtil eu = ExecutionUtil.instance;" +
        // "ExecutionUtil.dynamicProcessProperties = dynamicProcessProperties;" +
        "${script}; return dataContext")
    }
}
