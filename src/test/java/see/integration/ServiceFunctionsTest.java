package see.integration;

import org.junit.Test;
import see.See;
import see.tree.Node;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
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
        assertEquals(valueOf(9), eval("a = 4; a + 5;"));
        assertEquals(valueOf(9), eval("a = b = 4; a = 5; a + b;"));
    }

    @Test
    public void testLiterals() throws Exception {
        assertEquals(valueOf(1), eval("1;"));
        assertEquals(new BigDecimal("1.5"), eval("1.5;"));
        assertEquals("omg", eval("\"omg\";"));
        assertEquals("omg", eval("'omg';"));
    }

    @Test
    public void testIsDefined() throws Exception {
        assertEquals(Boolean.FALSE, eval("isDefined(a);"));
        assertEquals(Boolean.TRUE, eval("a = 5; isDefined(a);"));
        assertEquals(Boolean.FALSE, eval("a = 5; isDefined(b);"));
    }

    @Test
    public void testIf() throws Exception {
        assertEquals(valueOf(9), eval("if(1, 9, 42);"));
        assertEquals(valueOf(9), eval("if(0, 42, 9);"));
        assertEquals(valueOf(9), eval("if(1, 9);"));
        assertNull(eval("if(0, 9);"));
    }

    @Test
    public void testIncompleteIf() throws Exception {
        assertNull(eval("if (1) {} else {9;}"));
        assertNull(eval("if (0) {9;} else {}"));
        assertNull(eval("if (1) {} else {}"));
        assertNull(eval("if (0) {} else {}"));
    }

    @Test
    public void testNonCurlyIf() throws Exception {
        assertEquals(valueOf(9), eval("if(true) 9; else 42;"));
        assertEquals(valueOf(9), eval("if(false) 42; 9;"));
    }

    @Test
    public void testSequence() throws Exception {
        assertEquals(valueOf(9), eval("seq(9);"));
        assertEquals(valueOf(9), eval("seq(42, 9);"));
        assertEquals(valueOf(9), eval("42;9;"));
    }

    @Test
    public void testEmptySequence() throws Exception {
        assertNull(eval("seq();"));
    }

    @Test
    public void testIfLogic() throws Exception {
        assertEquals(valueOf(9), see.eval("if(true, 9, 42)"));
        assertEquals(valueOf(9), see.eval("if(false, 42, 9)"));
    }
}
