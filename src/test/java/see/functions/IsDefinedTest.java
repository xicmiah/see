package see.functions;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.functions.service.IsDefined;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;
import static see.functions.bool.BooleanCastHelper.toBoolean;

public class IsDefinedTest {
    private IsDefined isDefined = new IsDefined();

    private void assertTrue(Number value) {
        assertEquals(true, toBoolean(value));
    }
    private void assertFalse(Number value) {
        assertEquals(false, toBoolean(value));
    }

    @Test
    public void testOnStaticContext() throws Exception {
        Map<String, Object> context = ImmutableMap.<String, Object>of("c", 9);

        assertTrue(isDefined.apply(context).apply(of("c")));
        assertFalse(isDefined.apply(context).apply(of("a")));
    }

    @Test
    public void testOnChangingContext() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        Function<List<String>, BigDecimal> partial = isDefined.apply(context);

        assertFalse(partial.apply(of("c")));
        context.put("c", 9);
        assertTrue(partial.apply(of("c")));
    }
}
