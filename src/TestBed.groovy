class GroovyTestBed {

    private static scriptUnderTest = { dataContext ->
        // Your code goes after this

        for ( int i = 0; i < dataContext.getDataCount(); i++ ) {
            InputStream is = dataContext.getStream(i)
            Properties props = dataContext.getProperties(i)
            props.setProperty("key", "value")
            String result = "${is.text}\nGood bye World!"
            dataContext.storeStream(new ByteArrayInputStream(result.getBytes("UTF-8")), props)
        }

        // Your code goes before this
    }

    static void main(String[] args) throws Exception {

        println new ScriptRunner().run(
                scriptUnderTest,
                args.length == 0? "./DocumentData" : args[0]
        )
    }
}