class GroovyTestBed {
    static void main(String[] args) throws Exception {

        println new ScriptRunner().run(
                args.length == 0? "./script.groovy" : args[0],
                args.length < 2? "./DocumentData" : args[1]
        )
    }
}