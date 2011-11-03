package see.functions.compare;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GeqTest {

    Geq geq = new Geq();

    @Test
    public void testNumbers() throws Exception {
        assertTrue(geq.apply(ImmutableList.<Comparable>of(2.0, 1.0)));
        assertTrue(geq.apply(ImmutableList.<Comparable>of(2, 1)));
        assertTrue(geq.apply(ImmutableList.<Comparable>of(1, 1)));
    }

    @Test
    public void testStrings() throws Exception {
        assertTrue(geq.apply(ImmutableList.<Comparable>of("abc", "abc")));
        assertTrue(geq.apply(ImmutableList.<Comparable>of("abd", "abc")));
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertTrue(geq.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("1.0"))));
        assertTrue(geq.apply(ImmutableList.<Comparable>of(new BigDecimal("2"), new BigDecimal("1.0"))));
        assertFalse(geq.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("2.0"))));
        assertFalse(geq.apply(ImmutableList.<Comparable>of(new BigDecimal("1.0"), new BigDecimal("2.0"))));
    }
}
