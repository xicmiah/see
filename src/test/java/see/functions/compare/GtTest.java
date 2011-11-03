package see.functions.compare;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GtTest {

    Gt gt = new Gt();

    @Test
    public void testNumbers() throws Exception {
        assertTrue(gt.apply(ImmutableList.<Comparable>of(2.0, 1.0)));
        assertTrue(gt.apply(ImmutableList.<Comparable>of(2, 1)));
        assertFalse(gt.apply(ImmutableList.<Comparable>of(1, 1)));
    }

    @Test
    public void testStrings() throws Exception {
        assertFalse(gt.apply(ImmutableList.<Comparable>of("abc", "abc")));
        assertTrue(gt.apply(ImmutableList.<Comparable>of("abd", "abc")));
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertFalse(gt.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("1.0"))));
        assertTrue(gt.apply(ImmutableList.<Comparable>of(new BigDecimal("2"), new BigDecimal("1.0"))));
        assertFalse(gt.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("2.0"))));
        assertFalse(gt.apply(ImmutableList.<Comparable>of(new BigDecimal("1.0"), new BigDecimal("2.0"))));
    }
}
