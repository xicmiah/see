package see.functions.arithmetic;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Binary/unary minus operation.
 * Differentiates by argument count.
 */
public class Minus implements VarArgFunction<BigDecimal, BigDecimal> {
    @Override
    public BigDecimal apply(@Nonnull List<BigDecimal> input) {
        int size = input.size();
        checkArgument(size == 1 || size == 2, "Minus takes one or two arguments");

        if (size == 1) {
            return input.get(0).negate();
        } else {
            return input.get(0).subtract(input.get(1));
        }

    }

    @Override
    public String toString() {
        return "minus";
    }
}
