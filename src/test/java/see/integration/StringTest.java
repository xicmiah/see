package see.integration;

import org.junit.Test;
import see.See;

import static org.junit.Assert.assertEquals;

public class StringTest {

    See see = new See();

    @Test
    public void testAppend() throws Exception {
        assertEquals("45", see.eval("\"4\" + \"5\""));
    }

    @Test
    public void testNonStringArgs() throws Exception {
        assertEquals("a9", see.eval("'a' + 9"));
        assertEquals("anull", see.eval("'a' + null"));
    }
}
