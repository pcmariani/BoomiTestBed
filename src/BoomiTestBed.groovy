import boomitestbed.*

class BoomiTestBed {
    static void main(String[] args) throws Exception {
        def cli = new CliBuilder(usage: 'BoomiTestBed.groovy [-h] [-d document] [-p properties] script')

        cli.with {
            h  longOpt: 'help', 'Show usage information (you\'re here ;)'
            d  longOpt: 'document', args: 1, argName: 'documentName', 'Sets the source for incoming document'
            p  longOpt: 'properties', args: 1, argName: 'propertiesName', 'Sets the source properties'
            // xr longOpt: 'no result', 'don\t output result'
            // xp longOpt: 'no DPPs', 'don\t output dynamic process properties'
            // xd longOpt: 'no DDPs', 'don\t output dynamic document properties'
        }

        def options = cli.parse(args)

        if (options.h) {
            cli.usage()
            return
        }

        String scriptName = options.arguments()[0]
        String dataDocumentName = options.d ? options.d : null
        String propertiesFileName = options.p ? options.p : null

        if (scriptName == null) {
            cli.usage()
            return
        }

        println new ScriptRunner(new FileService(), new EvalService()).run(scriptName, dataDocumentName, propertiesFileName)
    }
}
