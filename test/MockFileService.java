import java.io.InputStream;
import java.util.HashMap;

class MockFileService implements MockableService {
    final HashMap<String, InputStream> inputStream = new HashMap<>();
    Boolean throwsException = false;
    String failsOn;

    public InputStream open(String fileName) throws Exception {
        if (throwsException && fileName.equals(failsOn)) {
            throw new Exception();
        }
        return inputStream.get(fileName);
    }

}