// package boomitestbed

class ScriptRunner2 {

    // String run(String scriptName, String dataDocumentName, String propertiesFileName, String outFileExtension, Boolean suppressData, Boolean suppressProps, String ddpPreplacePattern, Boolean outToFile) {
    String run(String scriptName, String dataDocumentName, String propertiesFileName, def options) {

        String outFileExtension = options.e ? options.e : "dat"
        String ddpPreplacePattern = options.rp ? options.rp : null
        Boolean outputScriptName = options.on
        Boolean outToFile = options.of
        Boolean outToDir = options.od
        Boolean suppressData = options.xd
        Boolean suppressProps = options.xp

        // TODO maybe
        // def commentInputs = getCommentInputs() // commentInputs.data .props. opts

        // SCRIPT
        String script = new FileInputStream(scriptName).text

        // DATA
        InputStream documentContents = new FileInputStream(dataDocumentName)

        // PROPS
        Properties properties = new Properties()
        properties.load(new FileInputStream(propertiesFileName) as InputStream)

        Properties dynamicProcessProperties = new Properties()
        Enumeration<?> e = properties.propertyNames()
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement()
            String value = properties.getProperty(key)

            def propFileGroup = (value =~ /(?i)^@file\(["'](.*?)["']\)/)
            if (propFileGroup.size() > 0) {
                ArrayList propsFileNameArr = propertiesFileName.split("/")
                def propsFilePath = propsFileNameArr[0..-2].join("/")
                String fileName = "${propsFilePath}/${propFileGroup[0][1]}"
                value = new FileInputStream(fileName).text
                    .replaceAll("\r?\n","")
                // println value
                properties.setProperty(key,value)
            }
            else {
                value = value.replaceAll("\\\\","\\\\\\\\")
            }

            if (!key.startsWith("document.dynamic.userdefined")) {
                dynamicProcessProperties.setProperty(key, value)
                properties.remove(key)
            }
        }

        // EVAL
        DataContext dataContext = new DataContext(documentContents, properties)
        ExecutionUtilHelper ExecutionUtil = new ExecutionUtilHelper()
        ExecutionUtil.dynamicProcessProperties = dynamicProcessProperties;
        script = script  -~ /import com[.]boomi[.]execution[.]ExecutionUtil;?/
        Eval.xy(dataContext, ExecutionUtil, "def dataContext = x; def ExecutionUtil = y;" +
                "${script}; return dataContext")

        def resultString = dataContext.printPretty()
        def dynamicProcessPropsString = formatProps(dynamicProcessProperties)
        def dynamicDocumentPropsString = formatProps(properties)

        // OUTPUT
        def output = ""
        if (!suppressData && !suppressProps) output += "\nResult\n------\n"
        if (!suppressData) output += resultString
        if (!suppressProps) {
            if (properties.propertyNames().hasMoreElements()) {
                output += "\nDynamic Document Props\n----------------------\n"
                output += dynamicDocumentPropsString
                    .replaceAll("document.dynamic.userdefined.","")
                    .replaceAll(/($ddpPreplacePattern?)=.*\n/,"\$1=...\n")
            }
            if (dynamicProcessProperties.propertyNames().hasMoreElements()) {
                output += "\n\nDYNAMIC PROCESS PROPS\n---------------------\n"
                output += dynamicProcessPropsString
            }
        }

        // SCIRPT NAME
        ArrayList scriptNameArr = scriptName.split("/")
        def scriptPath = scriptNameArr[0..-2].join("/")
        def scriptNameHead = scriptNameArr[-1] -~ /\.b\.groovy$/ -~ /\.groovy$/
        // println "scriptNameHead: " + scriptNameHead

        // WRITE FILES
        if (outToFile || outToDir) {
            def execFilesPath = scriptPath + "/_exec/" + ( outToDir ? scriptNameHead + '/' : "" )

            File executionFilesDir = new File(execFilesPath);
            if (! executionFilesDir.exists()) executionFilesDir.mkdir()

            File outDataFile = new File(execFilesPath + ( outToDir ? "" : scriptNameHead ) + "01_out." + outFileExtension)
            outDataFile.write resultString

            // if (propertiesFileName) {
            File outPropsFile = new File(execFilesPath + ( outToDir ? "" : scriptNameHead + "_" ) + "01_out.properties")
            outPropsFile.write dynamicProcessPropsString + "\n" + dynamicDocumentPropsString
            // }
        }

        return outputScriptName ? scriptNameHead : output
    }

    private String formatProps (Properties properties) {
        String propsString = ""
        Enumeration<?> e = properties.propertyNames()
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement()
            String value = properties.getProperty(key)
                                .replaceAll("\r?\n","")
                                .replaceAll("\\\\","\\\\\\\\")
            propsString += "${key}=${value}\n"
        }
        return propsString
    }

}
