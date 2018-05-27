import java.io.InputStream;
import java.util.HashMap;

public class MockFileService implements MockableService {
    HashMap<String, InputStream> inputStream = new HashMap<>();
    Boolean throwsExecption = false;
    String failsOn;

    public InputStream open(String fileName) throws Exception {
        if (throwsExecption && fileName == failsOn) {
            throw new Exception();
        }
        return inputStream.get(fileName);
    }

}