package see.parser;

import com.google.common.collect.ImmutableList;
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

    @Before
    public void setUp() throws Exception {
        Locale locale = new Locale("ru", "RU");
        LocalizedBigDecimalFactory numberFactory = new LocalizedBigDecimalFactory(MathContext.DECIMAL32, locale);
        see = new See(ConfigBuilder.defaultConfig().setNumberFactory(numberFactory).build());
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertEquals(valueOf(1.5), see.eval("1,5"));
    }

    @Test
    public void testExpressionParsing() throws Exception {
        assertEquals(valueOf(42.0), see.eval("31,7 + 10,3"));
    }

    @Test
    public void testMathContextInjection() throws Exception {
        assertEquals(valueOf(1).divide(valueOf(3), MathContext.DECIMAL32), see.eval("1/3"));
    }

    @Test
    public void testFunctionParameters() throws Exception {
        assertEquals(valueOf(42.0), see.eval("sum(31,7;10,3)"));
    }

    @Test
    public void testListLiterals() throws Exception {
        assertEquals(ImmutableList.of(valueOf(12.5), valueOf(42.9)), see.eval("[12,5;42,9]"));
    }

    @Test
    public void testMapLiterals() throws Exception {
        assertEquals(ImmutableMap.of("a", valueOf(42.9), "b", valueOf(9.5)), see.eval("{a:42,9;b:9,5}"));
    }
}
