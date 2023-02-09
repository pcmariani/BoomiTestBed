// import boomitestbed.*

class BoomiTestBed {
    static void main(String[] args) throws Exception {
        def cli = new CliBuilder(usage: 'BoomiTestBed.groovy [-hn] -s script [-d document] [-p properties] [-e extension] [-w working-dir]')

        cli.with {
            h  longOpt: 'help', 'Show usage (you\'re here ;)'
            s  longOpt: 'script', args: 1, argName: 'scriptName', 'Sets the source for the script'
            d  longOpt: 'document', args: 1, argName: 'documentName', 'Sets the source for incoming document'
            p  longOpt: 'properties', args: 1, argName: 'propertiesName', 'Sets the source properties'
            e  longOpt: 'extension', args: 1, argName: 'outFileExtension', 'Extension of output file'
            w  longOpt: 'working-dir', args: 1, argName: 'workingDir', 'Present Working Directory'
            xd longOpt: 'suppress-data-output', type: boolean, 'Suppresses output of data'
            xp longOpt: 'suppress-props-output', type: boolean, 'Suppresses output of props'
            rp longOpt: 'ddp-replace-pattern', args: 1, argName: 'ddpPreplacePattern', 'Props to prevent from appearing'
        }

        def options = cli.parse(args)

        if (options.h) {
            cli.usage()
            return
        }

        String workingDir = options.w ? options.w : System.getProperty("user.dir")
        String scriptName = options.s ? workingDir + "/" + options.s : null
        String dataDocumentName = options.d ? workingDir + "/" + options.d : null
        String propertiesFileName = options.p ? workingDir + "/" + options.p : null
        String outFileExtension = options.e ? options.e : "dat"
        Boolean suppressData = options.xd
        Boolean suppressProps = options.xp
        String ddpPreplacePattern = options.rp ? options.rp : null

        if (scriptName == null) {
            cli.usage()
            return
        }

        // println "PWD: " + System.getProperty("user.dir")
        // println "workingDir: " + options.w
        // println "scriptName: " + scriptName
        // println "dataDocumentName: " + dataDocumentName
        // println "propertiesFileName: " + propertiesFileName
        // println "outFileExtension: " + outFileExtension
        // println "Suppress Output: " + suppressResult

        println new ScriptRunner().run(scriptName, dataDocumentName, propertiesFileName, outFileExtension, suppressData, suppressProps, ddpPreplacePattern)
    }
}
