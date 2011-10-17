package see.functions.arithmetic;

import org.junit.Test;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;

public class PowerTest {

    final Power power = new Power();

    @Test
    public void testIntExponent() throws Exception {
        assertEquals(new BigDecimal(4), power.apply(of(new BigDecimal(2), new BigDecimal(2))));
        assertEquals(new BigDecimal(4), power.apply(of(new BigDecimal(-2), new BigDecimal(2))));
    }

    @Test
    public void testFractionalExponent() throws Exception {
        assertEquals(new BigDecimal(9), normalize(power.apply(of(new BigDecimal(81), new BigDecimal(0.5)))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBase() throws Exception {
        power.apply(of(new BigDecimal(-1), new BigDecimal(0.2)));
    }

    private BigDecimal normalize(Number number) {
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).stripTrailingZeros();
        } else {
            return new BigDecimal(number.doubleValue());
        }
    }
}
