import java.io.ByteArrayInputStream;

public class MockEvalService implements MockableService {
    boolean throwException = false;
    String expectedIsStream = "";

    public void eval(DataContext dataContext, String script) throws Exception {
        if (throwException) {
            throw new Exception("This is the reason.");
        }
        dataContext.setIs(new ByteArrayInputStream(expectedIsStream.getBytes()));
    }
}
