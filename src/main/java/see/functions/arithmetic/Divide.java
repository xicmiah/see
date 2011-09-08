package see.functions.arithmetic;

import see.functions.Function;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Divide implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Divide takes only two arguments");

        BigDecimal decimal = input.get(0);
        BigDecimal divisor = input.get(1);
        // TODO: Unhardcode math context
        return decimal.divide(divisor, MathContext.DECIMAL64);
    }
    @Override
    public String toString() {
        return "divide";
    }
}
