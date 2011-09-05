package see.functions.arithmetic;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Divide implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Divide takes only two arguments");

        return input.get(0).divide(input.get(1));
    }
    @Override
    public String toString() {
        return "divide";
    }
}
