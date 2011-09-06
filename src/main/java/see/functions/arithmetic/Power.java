package see.functions.arithmetic;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Power implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Power takes only two arguments");

        return input.get(0).pow(input.get(1).intValueExact());
    }

    @Override
    public String toString() {
        return "pow";
    }
}

