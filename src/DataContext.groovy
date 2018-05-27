class DataContext {
    InputStream is
    Properties props

    DataContext(InputStream inputStream, Properties properties){
        is = inputStream
        props = properties
    }

    void storeStream(InputStream inputStream, Properties properties){
        is = inputStream
        props = properties
    }

    int getDataCount(){
        1
    }
    InputStream getStream(int index){
        is
    }
    Properties getProperties(int index) {
        props
    }
}
