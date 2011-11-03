package see.functions.compare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqTest {

    Eq eq = new Eq();

    @Test
    public void testNumbers() throws Exception {
        assertTrue(eq.apply(ImmutableList.<Object>of(1.0, 1.0)));
        assertTrue(eq.apply(ImmutableList.<Object>of(1, 1)));
        assertTrue(eq.apply(ImmutableList.<Object>of(BigDecimal.valueOf(1), BigDecimal.valueOf(1))));
    }

    /**
     * Test that Eq can compare arbitrary Objects
     * @throws Exception
     */
    @Test
    public void testObjects() throws Exception {
        Object instance = new Object();
        assertTrue(eq.apply(ImmutableList.<Object>of(instance, instance)));
        assertFalse(eq.apply(ImmutableList.<Object>of(new Object(), new Object())));
    }

    /**
     * Test that Eq handles nulls
     * @throws Exception
     */
    @Test
    public void testNulls() throws Exception {
        assertTrue(eq.apply(Lists.newArrayList(null, null)));
        assertFalse(eq.apply(Lists.newArrayList(new Object(), null)));
        assertFalse(eq.apply(Lists.newArrayList(null, new Object())));
    }

    /**
     * Test that Eq compares BigDecimals via compareTo.
     * @throws Exception
     */
    @Test
    public void testBigDecimals() throws Exception {
        assertTrue(eq.apply(ImmutableList.<Object>of(new BigDecimal("1"), new BigDecimal("1.0"))));
    }
}
