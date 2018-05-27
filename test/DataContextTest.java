import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class DataContextTest {
    private DataContext dataContext;

    @Before
    public void setUp() {
        dataContext = new DataContext(new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        }, new Properties());
    }

    @Test
    public void getDataCount_shouldReturnOne_whenCalled() {
        int result = dataContext.getDataCount();

        assertEquals(1, result);
    }

    @Test
    public void getStream_shouldReturnIs_whenCalledWithAnyIndex() {
        int index = 0;
        String expectedContent = "The Content";
        InputStream expected = new ByteArrayInputStream(expectedContent.getBytes());
        dataContext = new DataContext(expected, new Properties());

        InputStream result = dataContext.getStream(index);

        assertEquals(expected, result);
    }

    @Test
    public void getProperties_shouldReturnProperties_whenCalledWithAnyIndex() {
        int index = 0;
        String isContent = "The Content";
        InputStream is = new ByteArrayInputStream(isContent.getBytes());
        Properties expected = new Properties();
        dataContext = new DataContext(is, expected);

        Properties result = dataContext.getProperties(index);

        assertEquals(expected, result);
    }

    @Test
    public void storeStream_shouldStoreBothValues_whenCalled() {
        String expectedContent = "The Content";
        InputStream expectedIs = new ByteArrayInputStream(expectedContent.getBytes());
        Properties expectedProperties = new Properties();

        dataContext.storeStream(expectedIs, expectedProperties);

        assertEquals(expectedProperties, dataContext.getProps());
        assertEquals(expectedIs, dataContext.getIs());
    }

}