// import boomitestbed.*

class BoomiTestBed {
    static void main(String[] args) throws Exception {
        def cli = new CliBuilder(usage: 'BoomiTestBed.groovy [-h][-on][-of][-od][-f][-xd][-xp] -s script [-d document] [-p properties] [-e extension] [-w working-dir] [-rp pattern]')

        cli.with {
            h  longOpt: 'help', 'Show usage'
            s  longOpt: 'script', args: 1, argName: 'fileName', 'Name of the script'
            d  longOpt: 'document', args: 1, argName: 'fileName', 'Name of input document'
            p  longOpt: 'properties', args: 1, argName: 'fileName', 'Name of input properties'
            on longOpt: 'output-name', type: boolean, 'Output script name no extension'
            of longOpt: 'output-files', type: boolean, 'Output files'
            od longOpt: 'output-dir', type: boolean, 'Output files indside a directory'
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

        // def dataFileList = []
        // def dir = new File(workingDir + '/_exec')
        // dir.eachFileRecurse (groovy.io.FileType.FILES) { file ->
        //     if (!file.name.contains(".properties")) dataFileList << file
        // }
        // dataFileList.each {
        //     println it.path - ~/$workingDir\//
        // }

        // println "PWD: " + System.getProperty("user.dir")
        // println "workingDir: " + options.w
        // println "scriptName: " + scriptName
        // println "dataDocumentName: " + dataDocumentName
        // println "propertiesFileName: " + propertiesFileName

        if (scriptName == null) {
            cli.usage()
            return
        }

        // println new ScriptRunner2().run(scriptName, dataDocumentName, propertiesFileName, outFileExtension, suppressData, suppressProps, ddpPreplacePattern, outToFile)
        println new ScriptRunner2().run(scriptName, dataDocumentName, propertiesFileName, options)
    }
}
