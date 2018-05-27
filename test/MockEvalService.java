import java.io.ByteArrayInputStream;

class MockEvalService implements MockableService {
    boolean throwException = false;
    String expectedIsStream = "";

    @SuppressWarnings("unused")
    public void eval(DataContext dataContext, String script) throws Exception {
        if (throwException) {
            throw new Exception("This is the reason.");
        }
        dataContext.setIs(new ByteArrayInputStream(expectedIsStream.getBytes()));
    }
}
