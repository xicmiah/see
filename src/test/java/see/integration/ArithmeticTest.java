package see.integration;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.See;
import see.tree.Node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

public class ArithmeticTest {
    private final BigDecimal nine = valueOf(9);

    protected Object eval(String expression, Map<String, Object> context) {
        See see = new See();
        Node<Object> tree = see.parseExpression(expression);
        return see.evaluate(tree, context);
    }

    protected Object eval(String expression) {
        return eval(expression, new HashMap<String, Object>());
    }

    @Test
    public void testArithmetic() throws Exception {
        assertEquals(nine, eval("4+5"));
        assertEquals(nine, eval("42-33"));
        assertEquals(nine, eval("3*3"));
        assertEquals(nine, eval("54/6"));
        
        assertEquals(nine, eval("4 + 2 * 3 - 5 / 5"));
        assertEquals(nine, eval("((4 + (2*3)) - (5/5))"));
    }

    @Test
    public void testUnaryPlusMinus() throws Exception {
        assertEquals(valueOf(-9), eval("-9"));
        assertEquals(valueOf(9), eval("+9"));
        assertEquals(valueOf(9), eval("-(-9)"));
    }

    @Test
    public void testLogicalOps() throws Exception {
        assertEquals(valueOf(0), eval("!9"));
        assertEquals(valueOf(0), eval("!1"));
        assertEquals(valueOf(1), eval("!0"));
    }

    @Test
    public void testIdentifiers() throws Exception {
        assertEquals(nine, eval("a-b", ImmutableMap.<String, Object>of("a", valueOf(10), "b", valueOf(1))));
        assertEquals(nine, eval("a_b", ImmutableMap.<String, Object>of("a_b", valueOf(9))));
    }
}
