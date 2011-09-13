package see.functions.arithmetic;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Power implements Function<List<BigDecimal>, Number> {
    @Override
    public Number apply(List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Power takes only two arguments");

        return power(input.get(0), input.get(1));
    }

    private Number power(BigDecimal base, BigDecimal exponent) {
        if (isInteger(exponent)) {
            return base.pow(exponent.intValue());
        } else {
            return Math.pow(base.doubleValue(), exponent.doubleValue());
        }
    }

    private boolean isInteger(BigDecimal exponent) {
        return exponent.stripTrailingZeros().scale() <= 0;
    }

    @Override
    public String toString() {
        return "pow";
    }
}

