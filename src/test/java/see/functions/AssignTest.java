package see.functions;

import org.junit.Test;
import see.functions.service.Assign;
import see.functions.service.VarAsSettable;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;

public class AssignTest {
    Assign<Integer> assign = new Assign<Integer>();
    VarAsSettable varAsSettable = new VarAsSettable();

    @Test
    public void testApply() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("a", 42);

        assign.apply(of(settableFor(context, "a"), 9));

        assertEquals(9, context.get("a"));
    }

    @Test
    public void testInitialization() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();

        assign.apply(of(settableFor(context, "c"), 9));

        assertEquals(9, context.get("c"));
    }

    private Settable<Object> settableFor(final Map<String, Object> context, final String var) {
        return new Settable<Object>() {
            @Override
            public void set(Object value) {
                context.put(var, value);
            }
        };
    }
}
