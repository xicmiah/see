package see.integration;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import see.See;
import see.exceptions.EvaluationException;
import see.functions.Function;
import see.parser.config.ConfigBuilder;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

public class ArithmeticTest {
    final BigDecimal nine = valueOf(9);
    final Boolean positive = true;
    final Boolean negative = false;

    See see;

    @Before
    public void setUp() throws Exception {
        see = new See(ConfigBuilder.defaultConfig().addPureFunction("fail", new Function<List<Object>, Object>() {
            @Override
            public Object apply(List<Object> input) {
                throw new IllegalStateException("Fail evaluated");
            }
        }).build());
    }

    @Test
    public void testArithmetic() throws Exception {
        assertEquals(nine, see.eval("4+5"));
        assertEquals(nine, see.eval("42-33"));
        assertEquals(nine, see.eval("3*3"));
        assertEquals(nine, see.eval("54/6"));

        assertEquals(nine, see.eval("4 + 2 * 3 - 5 / 5"));
        assertEquals(nine, see.eval("((4 + (2*3)) - (5/5))"));
    }

    @Test
    public void testPower() throws Exception {
        assertEquals(nine, see.eval("3^2"));
        assertEquals(9, ((Number) see.eval("81^0.5")).intValue());
    }

    @Test
    public void testDivision() throws Exception {
        assertEquals(1.0D / 3, ((Number) see.eval("1/3")).doubleValue(), 1e-9);
    }

    @Test
    public void testUnaryPlusMinus() throws Exception {
        assertEquals(valueOf(-9), see.eval("-9"));
        assertEquals(valueOf(9), see.eval("+9"));
        assertEquals(valueOf(9), see.eval("-(-9)"));
    }

    @Test
    public void testLogicalOps() throws Exception {
        assertEquals(negative, see.eval("!true"));
        assertEquals(positive, see.eval("!false"));

        assertEquals(positive, see.eval("true || true"));
        assertEquals(positive, see.eval("true || false"));
        assertEquals(positive, see.eval("false || true"));
        assertEquals(negative, see.eval("false || false"));

        assertEquals(positive, see.eval("true && true"));
        assertEquals(negative, see.eval("true && false"));
        assertEquals(negative, see.eval("false && true"));
        assertEquals(negative, see.eval("false && false"));
    }

    @Test(expected = EvaluationException.class)
    public void testLogicalShortCircuit() throws Exception {
        assertEquals(negative, see.eval("true && fail()"));
        assertEquals(positive, see.eval("false || fail()"));
    }

    @Test
    public void testMinMax() throws Exception {
        assertEquals(nine, see.eval("max(-100500, -9000, 9, 4.2)"));
        assertEquals(nine, see.eval("min(100500, 9, 42)"));
    }

    @Test
    public void testIdentifiers() throws Exception {
        ImmutableMap<String, Object> context = ImmutableMap.<String, Object>builder()
                .put("a", valueOf(10))
                .put("b", valueOf(1))
                .put("a_b", valueOf(9))
                .build();
        assertEquals(nine, see.eval("a-b", context));
        assertEquals(nine, see.eval("a_b", context));
    }
}
