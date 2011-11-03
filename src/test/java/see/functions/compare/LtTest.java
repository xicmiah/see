
package see.functions.compare;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LtTest {
    Lt lt = new Lt();

    @Test
    public void testNumbers() throws Exception {
        assertTrue(lt.apply(ImmutableList.<Comparable>of(1.0, 2.0)));
        assertFalse(lt.apply(ImmutableList.<Comparable>of(2, 1)));
        assertFalse(lt.apply(ImmutableList.<Comparable>of(1, 1)));
    }

    @Test
    public void testStrings() throws Exception {
        assertFalse(lt.apply(ImmutableList.<Comparable>of("abc", "abc")));
        assertFalse(lt.apply(ImmutableList.<Comparable>of("abd", "abc")));
        assertTrue(lt.apply(ImmutableList.<Comparable>of("abd", "bbc")));
    }

    @Test
    public void testBigDecimals() throws Exception {
        assertFalse(lt.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("1.0"))));
        assertFalse(lt.apply(ImmutableList.<Comparable>of(new BigDecimal("2"), new BigDecimal("1.0"))));
        assertTrue(lt.apply(ImmutableList.<Comparable>of(new BigDecimal("1"), new BigDecimal("2.0"))));
        assertTrue(lt.apply(ImmutableList.<Comparable>of(new BigDecimal("1.0"), new BigDecimal("2.0"))));
    }
}
