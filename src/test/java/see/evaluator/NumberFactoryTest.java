package see.evaluator;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.See;
import see.parser.config.ConfigBuilder;
import see.parser.numbers.BigDecimalFactory;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class NumberFactoryTest {
    See see = new See(ConfigBuilder.defaultConfig().setNumberFactory(new BigDecimalFactory()).build());

    /**
     * Test that type of numbers is raised to type of NumberFactory
     * @throws Exception
     */
    @Test
    public void testTypeRaising() throws Exception {
        Map<String, Object> context = ImmutableMap.<String, Object>of("a", 42, "c", 9.0);
        
        assertThat(see.eval("a", context), instanceOf(BigDecimal.class));
        assertThat(see.eval("c", context), instanceOf(BigDecimal.class));
        assertThat(see.eval("9", context), instanceOf(BigDecimal.class));
        assertThat(see.eval("9.42", context), instanceOf(BigDecimal.class));
    }

    /**
     * Test that non-numbers are passed directly.
     * @throws Exception
     */
    @Test
    public void testNonNumbers() throws Exception {
        Object unknown = new Object();
        Map<String, Object> context = ImmutableMap.of("s", "asd", "c", unknown);

        assertEquals("asd", see.eval("s", context));
        assertSame(unknown, see.eval("c", context));
    }
}
