import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ScriptRunnerTest {

    private MockFileService mockFileService;
    private MockEvalService mockEvalService;
    private ScriptRunner scriptRunner;

    @Before
    public void setUp() {
        String documentContent = "Content";
        String propertiesContent = "key=value";
        String scriptContent = "2 + 2";

        mockFileService = new MockFileService();
        mockEvalService = new MockEvalService();
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);

        InputStream documentStream = new ByteArrayInputStream(documentContent.getBytes());
        InputStream propertiesStream = new ByteArrayInputStream(propertiesContent.getBytes());
        InputStream scriptStream = new ByteArrayInputStream(scriptContent.getBytes());

        mockFileService.inputStream.put("documentName", documentStream);
        mockFileService.inputStream.put("scriptName", scriptStream);
        mockFileService.inputStream.put("propertiesName", propertiesStream);
    }

    @Test
    public void run_shouldReturnErrorMessage_whenOpeningScriptFails() {
        mockFileService.throwsExecption = true;
        mockFileService.failsOn = "scriptName";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);

        String result = scriptRunner.run(
                "scriptName",
                "documentName",
                "propertiesName"
        );

        assertEquals("I can't find the script scriptName", result);
    }

    @Test
    public void run_shouldReturnErrorMessage_whenOpeningDocumentFails() {
        mockFileService.throwsExecption = true;
        mockFileService.failsOn = "documentName";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);

        String result = scriptRunner.run(
                "scriptName",
                "documentName",
                "propertiesName"
        );

        assertEquals("I can't find the document documentName", result);
    }

    @Test
    public void run_shouldReturnErrorMessage_whenOpeningPropertyFails() {
        mockFileService.throwsExecption = true;
        mockFileService.failsOn = "propertiesName";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);

        String result = scriptRunner.run(
                "scriptName",
                "documentName",
                "propertiesName"
        );

        assertEquals("I can't find the properties propertiesName", result);
    }

    @Test
    public void run_shouldReturnOriginalDocument_whenScriptDoesNotChangeIt() {
        mockEvalService.expectedIsStream = "Content";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);
        String result = scriptRunner.run(
                "scriptName",
                "documentName",
                "propertiesName"
        );

        assertEquals("Resulting Document\nContent\nResulting Props\n[key:value]", result);
    }

    @Test
    public void run_shouldReturnOriginalProperties_whenScriptDoesNotChangeIt() {
        mockEvalService.expectedIsStream = "Content";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);
        String result = scriptRunner.run(
                "scriptName",
                "documentName",
                "propertiesName"
        );

        assertEquals("Resulting Document\nContent\nResulting Props\n[key:value]", result);
    }

    @Test
    public void run_shouldCreateBlankDocument_whenDocumentNameNull() {
        String result = scriptRunner.run("scriptName", null, "propertiesName");

        assertEquals("Resulting Document\n\nResulting Props\n[key:value]", result);
    }

    @Test
    public void run_shouldCreateBlankProperties_whenPropertiesNameNull() {
        mockEvalService.expectedIsStream = "Content";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);
        String result = scriptRunner.run(
                "scriptName",
                "documentName",
                null
        );

        assertEquals("Resulting Document\nContent\nResulting Props\n[:]", result);
    }

    @Test
    public void run_shouldReturnTransformedDoucment_whenCalledWithValidScript() {
        mockEvalService.expectedIsStream = "TransformedDocument";
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);

        String result = scriptRunner.run(
                "scriptName",
                null,
                null
        );

        assertEquals("Resulting Document\nTransformedDocument\nResulting Props\n[:]", result);
    }

    @Test
    public void run_shouldReturnErrorMessage_whenScriptDoesNotEvaluate() {
        mockEvalService.throwException = true;
        scriptRunner = new ScriptRunner(mockFileService, mockEvalService);
        String result = scriptRunner.run(
                "scriptName",
                null,
                null
        );

        assertEquals("That script does not make sense to me:\n This is the reason.", result);
    }

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
}