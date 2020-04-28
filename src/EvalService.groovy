class EvalService implements MockableService {
    void eval(DataContext dataContext, Properties dynamicProcessProperties, String script) {
        Eval.xy(dataContext, dynamicProcessProperties, "def dataContext = x; def dynamicProcessProperties = y;" +
        "ExecutionUtil.dynamicProcessProperties = dynamicProcessProperties;" +
        "${script}; return dataContext")
    }
}
