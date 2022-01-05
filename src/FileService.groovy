// package boomitestbed

class FileService implements MockableService {
    InputStream open(String fileName) {
        new FileInputStream(fileName)
    }
}
