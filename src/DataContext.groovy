class DataContext {
    InputStream is
    Properties props

    void storeStream(InputStream inputStream, Properties properties){
        is = inputStream
        props = properties
    }
    static int getDataCount(){
        1
    }
    InputStream getStream(int index){
        is
    }
    Properties getProperties(int index) {
        props
    }
}
