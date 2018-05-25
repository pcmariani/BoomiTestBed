for ( int i = 0; i < dataContext.getDataCount(); i++ ) {
    InputStream is = dataContext.getStream(i)
    Properties props = dataContext.getProperties(i)
    props.setProperty("key", "value")
    String result = "${is.text}\nGood bye World!"
    dataContext.storeStream(new ByteArrayInputStream(result.getBytes("UTF-8")), props)
}