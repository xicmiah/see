package see.integration;

import org.junit.Test;
import see.See;
import see.exceptions.SeeRuntimeException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StringTest {

    See see = new See();

    @Test
    public void testAppend() throws Exception {
        assertEquals("45", see.eval("\"4\" + \"5\""));
    }

    @Test
    public void testArithmeticConcat() throws Exception {
        try {
            see.eval("\"4\"+5");
            throw new AssertionError("No exception is raised");
        } catch (SeeRuntimeException e) {
            assertThat(e.getCause(), instanceOf(ClassCastException.class));
        }
    }


}
