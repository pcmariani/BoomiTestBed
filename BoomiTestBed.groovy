// import boomitestbed.*

class BoomiTestBed {
    static void main(String[] args) throws Exception {
        def cli = new CliBuilder(usage: 'BoomiTestBed.groovy [-h][-n][-f][-xd][-xp] -s script [-d document] [-p properties] [-e extension] [-w working-dir] [-rp pattern]')

        cli.with {
            h  longOpt: 'help', 'Show usage'
            s  longOpt: 'script', args: 1, argName: 'fileName', 'Name of the script'
            d  longOpt: 'document', args: 1, argName: 'fileName', 'Name of input document'
            p  longOpt: 'properties', args: 1, argName: 'fileName', 'Name of input properties'
            f  longOpt: 'output-file', type: boolean, 'Output to file'
            e  longOpt: 'extension', args: 1, argName: 'fileExtension', 'Extension of output file'
            w  longOpt: 'working-dir', args: 1, argName: 'dir', 'Present Working Directory'
            xd longOpt: 'suppress-data-output', type: boolean, 'Suppresses output of data'
            xp longOpt: 'suppress-props-output', type: boolean, 'Suppresses output of props'
            rp longOpt: 'ddp-replace-pattern', args: 1, argName: 'regex', 'Prevent props from appearing'
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
        Boolean outToFile = options.f
        Boolean suppressData = options.xd
        Boolean suppressProps = options.xp
        String ddpPreplacePattern = options.rp ? options.rp : null

        // println "PWD: " + System.getProperty("user.dir")
        // println "workingDir: " + options.w
        // println "scriptName: " + scriptName
        // println "dataDocumentName: " + dataDocumentName
        // println "propertiesFileName: " + propertiesFileName
        // println "outFileExtension: " + outFileExtension
        // println "outToFile: " + outToFile
        // println "Suppress Output: " + suppressResult

        if (scriptName == null) {
            cli.usage()
            return
        }

        println new ScriptRunner2().run(scriptName, dataDocumentName, propertiesFileName, outFileExtension, suppressData, suppressProps, ddpPreplacePattern, outToFile)
    }
}
