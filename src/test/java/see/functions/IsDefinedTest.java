package see.functions;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.functions.service.IsDefined;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsDefinedTest {
    private IsDefined isDefined = new IsDefined();

    @Test
    public void testOnStaticContext() throws Exception {
        Map<String, Object> context = ImmutableMap.<String, Object>of("c", 9);
        
        assertTrue(isDefined.apply(context).apply(of("c")));
        assertFalse(isDefined.apply(context).apply(of("a")));
    }

    @Test
    public void testOnChangingContext() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        Function<List<String>, Boolean> partial = isDefined.apply(context);

        assertFalse(partial.apply(of("c")));
        context.put("c", 9);
        assertTrue(partial.apply(of("c")));
    }
}
