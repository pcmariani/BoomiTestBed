import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class GroovyTestBedTest {
    private String[] args;
    ByteArrayOutputStream myOut;

    @Before
    public void setUp() {
        myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
    }

    @Test
    public void main_shouldPrintUsage_whenCalledWithoutScript() throws Exception {
        String expected = "usage: GroovyTestBed.groovy [-h] [-d document] [-p properties] script\n" +
                " -d,--document <documentName>       Sets the source for incoming document\n" +
                " -h,--help                          Show usage information (you're here ;)\n" +
                " -p,--properties <propertiesName>   Sets the source properties\n";
        args = new String[]{};

        GroovyTestBed.main(args);

        assertEquals(expected, myOut.toString());
    }

    @Test
    public void main_shouldPrintUsage_whenCalledWithHelpParameter() throws Exception {
        String expected = "usage: GroovyTestBed.groovy [-h] [-d document] [-p properties] script\n" +
                " -d,--document <documentName>       Sets the source for incoming document\n" +
                " -h,--help                          Show usage information (you're here ;)\n" +
                " -p,--properties <propertiesName>   Sets the source properties\n";
        args = new String[]{"-h"};

        GroovyTestBed.main(args);

        assertEquals(expected, myOut.toString());
    }

    @Test
    public void main_shouldPrintResults_whenCalledWithScript() throws Exception {
        String expected = "Resulting Document\n" +
                "\n" +
                "Good bye World!\n" +
                "Resulting Props\n" +
                "[key:value]\n";
        args = new String[]{"script.groovy"};

        GroovyTestBed.main(args);

        assertEquals(expected, myOut.toString());
    }

    @Test
    public void main_shouldUseProvidedDocument_WhenDocumentParameterIsUsed() throws Exception {
        String expected = "Resulting Document\n" +
                "Hello World!\n" +
                "Good bye World!\n" +
                "Resulting Props\n" +
                "[key:value]\n";
        args = new String[]{"-d", "DocumentData", "script.groovy"};

        GroovyTestBed.main(args);

        assertEquals(expected, myOut.toString());
    }

    @Test
    public void main_shouldUseProvidedProperties_whenPropertiesParameterIsUsed() throws Exception {
        String expected = "Resulting Document\n" +
                "\n" +
                "Good bye World!\n" +
                "Resulting Props\n" +
                "[key:value, dog:cat]\n";
        args = new String[]{"-p", "test.properties", "script.groovy"};

        GroovyTestBed.main(args);

        assertEquals(expected, myOut.toString());
    }

    @Test
    public void main_shouldUseAllParameters_whenCalledWithMultipleParameters() throws Exception {
        String expected = "Resulting Document\n" +
                "Hello World!\n" +
                "Good bye World!\n" +
                "Resulting Props\n" +
                "[key:value, dog:cat]\n";
        args = new String[] {"-d", "DocumentData", "-p", "test.properties", "script.groovy"};

        GroovyTestBed.main(args);

        assertEquals(expected, myOut.toString());
    }
}