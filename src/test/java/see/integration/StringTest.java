package see.integration;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import see.See;
import see.functions.Function;
import see.parser.config.ConfigBuilder;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

public class StringTest {

    See see;

    @Before
    public void setUp() throws Exception {
        see = new See(ConfigBuilder.defaultConfig().addPureFunction("fail", new Function<List<Object>, Boolean>() {
            @Override
            public Boolean apply(List<Object> input) {
                throw new IllegalStateException("Fail evaluated");
            }
        }).build());
    }

    @Test
    public void testAppend() throws Exception {
        assertEquals("45", see.eval("\"4\" + \"5\""));
    }

    @Test
    public void testArithmeticConcat() throws Exception {
        try {
            see.eval("\"4\"+5");
        } catch (Exception e) {
            if (!(e.getCause() instanceof ClassCastException)) {
                throw e;
            }
            return;
        }
        throw new Exception("No exception is raised");
    }
}
