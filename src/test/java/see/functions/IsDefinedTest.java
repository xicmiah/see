package see.functions;

import org.junit.Test;
import see.functions.service.IsDefined;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsDefinedTest {
    private IsDefined isDefined = new IsDefined();

    @Test
    public void testOnStaticContext() throws Exception {
        assertTrue(isDefined.apply(Arrays.<Object>asList("c")));
        assertFalse(isDefined.apply(Arrays.asList(new Object[]{null})));
    }
}
