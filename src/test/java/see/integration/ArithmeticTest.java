package see.integration;

import org.junit.Test;
import see.See;
import see.tree.Node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ArithmeticTest {
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
        BigDecimal nine = BigDecimal.valueOf(9);
        
        assertEquals(nine, eval("4+5"));
        assertEquals(nine, eval("42-33"));
        assertEquals(nine, eval("3*3"));
        assertEquals(nine, eval("54/6"));
        
        assertEquals(nine, eval("4 + 2 * 3 - 5 / 5"));
        assertEquals(nine, eval("((4 + (2*3)) - (5/5))"));
    }
}
