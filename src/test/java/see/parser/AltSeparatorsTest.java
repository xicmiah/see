package see.parser;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import see.See;
import see.parser.config.ConfigBuilder;
import see.parser.numbers.LocalizedBigDecimalFactory;

import java.math.MathContext;
import java.util.Locale;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

public class AltSeparatorsTest {
    See see;
    final ImmutableMap<String,Object> emptyContext = ImmutableMap.of();

    @Before
    public void setUp() throws Exception {
        Locale locale = new Locale("ru", "RU");
        LocalizedBigDecimalFactory numberFactory = new LocalizedBigDecimalFactory(MathContext.DECIMAL32, locale);
        see = new See(ConfigBuilder.defaultConfig().setNumberFactory(numberFactory).build());
    }

    private Object eval(String expression) {
        return see.evaluate(see.parseExpression(expression), emptyContext);
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertEquals(valueOf(1.5), eval("1,5"));
    }

    @Test
    public void testExpressionParsing() throws Exception {
        assertEquals(valueOf(42.0), eval("31,7 + 10,3"));
    }

    @Test
    public void testMathContextInjection() throws Exception {
        assertEquals(valueOf(1).divide(valueOf(3), MathContext.DECIMAL32), eval("1/3"));
    }

    @Test
    public void testFunctionParameters() throws Exception {
        assertEquals(valueOf(42.0), eval("sum(31,7;10,3)"));
    }
}
