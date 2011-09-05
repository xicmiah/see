package see.integration;

import org.junit.Ignore;
import org.junit.Test;
import see.See;
import see.exceptions.EvaluationException;
import see.tree.Node;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ServiceFunctionsTest {
    See see = new See();

    private Object eval(String expression) {
        Node<Object> tree = see.parseExpressionList(expression);
        return see.evaluate(tree);
    }

    @Test
    public void testAssign() throws Exception {
        assertEquals(BigDecimal.valueOf(9), eval("a = 4; a + 5"));
        assertEquals(BigDecimal.valueOf(9), eval("a = b = 4; a = 5; a + b"));
    }

    @Ignore("not implemented properly")
    @Test
    public void testIsDefined() throws Exception {
        assertEquals(BigDecimal.ZERO, eval("isDefined(a)"));
        assertEquals(BigDecimal.ONE, eval("a = 5; isDefined(a)"));
        assertEquals(BigDecimal.ONE, eval("a = 5; isDefined(b)"));
    }

    @Test
    public void testIf() throws Exception {
        assertEquals(BigDecimal.valueOf(9), eval("if(1, 9, 42)"));
        assertEquals(BigDecimal.valueOf(9), eval("if(0, 42, 9)"));
        assertEquals(BigDecimal.valueOf(9), eval("if(1, 9)"));
        assertNull(eval("if(0, 9)"));
    }

    @Test
    public void testSequence() throws Exception {
        assertEquals(BigDecimal.valueOf(9), eval("seq(9)"));
        assertEquals(BigDecimal.valueOf(9), eval("seq(42, 9)"));
        assertEquals(BigDecimal.valueOf(9), eval("42;9"));
    }

    @Test(expected = EvaluationException.class)
    public void testEmptySequence() throws Exception {
        eval("seq()");
    }
}
