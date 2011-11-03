package see.functions.compare;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LeqTest {

    Leq leq = new Leq();

    @Test
    public void testNumbers() throws Exception {
        assertTrue(leq.apply(ImmutableList.<Comparable>of(1.0, 2.0)));
        assertFalse(leq.apply(ImmutableList.<Comparable>of(2, 1)));
        assertTrue(leq.apply(ImmutableList.<Comparable>of(1, 1)));
    }

    @Test
    public void testStrings() throws Exception {
        assertTrue(leq.apply(ImmutableList.<Comparable>of("abc", "abc")));
        assertFalse(leq.apply(ImmutableList.<Comparable>of("abd", "abc")));
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertTrue(leq.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("1.0"))));
        assertFalse(leq.apply(ImmutableList.<Comparable>of(new BigDecimal("2"), new BigDecimal("1.0"))));
        assertTrue(leq.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("2.0"))));
        assertTrue(leq.apply(ImmutableList.<Comparable>of(new BigDecimal("1.0"), new BigDecimal("2.0"))));
    }
}
