class EvalService implements MockableService {
    void eval(DataContext dataContext, Properties dynamicProcessProperties, String script) {
        Eval.xy(dataContext, dynamicProcessProperties, "def dataContext = x; def dynamicProcessProperties = y;" +
        "ExecutionUtil.dynamicProcessProperties = dynamicProcessProperties;" +
        "${script}; return dataContext")
    }
}


// class EvalService implements MockableService {
//     void eval(DataContext dataContext, String script) {
//         Eval.x(dataContext, "def dataContext = x; ${script} ; return dataContext")
//     }
// }
