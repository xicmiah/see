package see.functions;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AssignTest {
    Assign<Integer> assign;

    @Before
    public void setUp() throws Exception {
        assign = new Assign<Integer>();
    }

    @Test
    public void testApply() throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("a", 42);
        Assign.VariableAndValue<Integer> var = new Assign.VariableAndValue<Integer>("a", 9);
        var.setContext(context);

        assign.apply(ImmutableList.of(var));

        assertEquals(9, context.get("a"));
    }
}
