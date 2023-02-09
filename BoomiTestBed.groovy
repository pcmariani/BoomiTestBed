// import boomitestbed.*

class BoomiTestBed {
    static void main(String[] args) throws Exception {
        def cli = new CliBuilder(usage: 'BoomiTestBed.groovy [-h] [-d document] [-p properties] script')

        cli.with {
            h  longOpt: 'help', 'Show usage information (you\'re here ;)'
            s  longOpt: 'script', args: 1, argName: 'scriptName', 'Sets the source for the script'
            d  longOpt: 'document', args: 1, argName: 'documentName', 'Sets the source for incoming document'
            p  longOpt: 'properties', args: 1, argName: 'propertiesName', 'Sets the source properties'
            n  longOpt: 'no-result', type: boolean, 'suppresses output' 
            o  longOpt: 'extension', args: 1, argName: 'outFileExtension', 'Extension of output file' 

            // xr longOpt: 'no result', 'don\t output result'
            // xp longOpt: 'no DPPs', 'don\t output dynamic process properties'
            // xd longOpt: 'no DDPs', 'don\t output dynamic document properties'
        }

        def options = cli.parse(args)

        if (options.h) {
            cli.usage()
            return
        }

        println "Suppress Output: " + options.n.toString()
        println "Extension: " + options.o

        // String scriptName = options.arguments()[0] ? options.arguments()[0] : ( options.s ? options.s : null)
        String scriptName = options.s ? options.s : ( options.arguments()[0] ? options.arguments()[0] : null)
        String dataDocumentName = options.d ? options.d : null
        String propertiesFileName = options.p ? options.p : null
        String outFileExtension = options.o ? options.o : "dat"
        Boolean suppressResult = options.n

        if (scriptName == null) {
            cli.usage()
            return
        }

        println new ScriptRunner(new FileService(), new EvalService())
                    .run(scriptName, dataDocumentName, propertiesFileName, outFileExtension, suppressResult)
    }
}
