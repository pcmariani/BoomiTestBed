class GroovyTestBed {
    static void main(String[] args) throws Exception {
        def cli = new CliBuilder(usage: 'GroovyTestBed.groovy [-h] [-d document] [-p properties] script')

        cli.with {
            h longOpt: 'help', 'Show usage information (you\'re here ;)'
            d longOpt: 'document', args: 1, argName: 'documentName', 'Sets the source for incoming document'
            p longOpt: 'properties', args: 1, argName: 'propertiesName', 'Sets the source properties'
        }

        def options = cli.parse(args)

        if (options.h) {
            cli.usage()
            return
        }

        String dataDocumentName = options.d ? options.d : null
        String propertiesFileName = options.p ? options.p : null
        String scriptName = options.arguments()[0]

        if (scriptName == null) {
            cli.usage()
            return
        }

        println new ScriptRunner(new FileService(), new EvalService()).run(scriptName, dataDocumentName, propertiesFileName)
    }
}