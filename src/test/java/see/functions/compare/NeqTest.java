package see.functions.compare;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NeqTest {
     Neq neq = new Neq();

    @Test
    public void testNumbers() throws Exception {
        assertFalse(neq.apply(ImmutableList.<Object>of(1.0, 1.0)));
        assertTrue(neq.apply(ImmutableList.<Object>of(1, 2)));
        assertTrue(neq.apply(ImmutableList.<Object>of(BigDecimal.valueOf(2), BigDecimal.valueOf(1))));
    }

    @Test
    public void testObjects() throws Exception {
        Object instance = new Object();
        assertFalse(neq.apply(ImmutableList.<Object>of(instance, instance)));
        assertTrue(neq.apply(ImmutableList.<Object>of(new Object(), new Object())));
    }

    @Test
    public void testNulls() throws Exception {
        assertFalse(neq.apply(Lists.newArrayList(null, null)));
        assertTrue(neq.apply(Lists.newArrayList(new Object(), null)));
        assertTrue(neq.apply(Lists.newArrayList(null, new Object())));
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertFalse(neq.apply(ImmutableList.<Object>of(new BigDecimal("1"), new BigDecimal("1.0"))));
    }
}
