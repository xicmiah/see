package see.functions;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AssignTest {
    Assign<Integer> assign = new Assign<Integer>();

    @Test
    public void testApply() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("a", 42);

        assign.apply(context).apply(ImmutableList.<Object>of("a", 9));

        assertEquals(9, context.get("a"));
    }

    @Test
    public void testInitialization() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        
        assign.apply(context).apply(ImmutableList.<Object>of("c", 9));

        assertEquals(9, context.get("c"));
    }
}
