package see.functions;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.evaluation.Context;
import see.functions.service.IsDefined;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static see.evaluation.evaluators.SimpleContext.fromMutable;

public class IsDefinedTest {
    private IsDefined isDefined = new IsDefined();

      @Test
    public void testOnStaticContext() throws Exception {
          Map<String, Object> contents = ImmutableMap.<String, Object>of("c", 9);
          Context context = fromMutable(contents);

          assertTrue(isDefined.apply(context).apply(of("c")));
          assertFalse(isDefined.apply(context).apply(of("a")));
    }

    @Test
    public void testOnChangingContext() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        Function<List<String>,Boolean> partial = isDefined.apply(fromMutable(context));

        assertFalse(partial.apply(of("c")));
        context.put("c", 9);
        assertTrue(partial.apply(of("c")));
    }

}
