package see.functions.arithmetic;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

public class Product implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        BigDecimal result = BigDecimal.ONE;

        for (BigDecimal value : input) {
            result = result.multiply(value);
        }

        return result;
    }

    @Override
    public String toString() {
        return "product";
    }
}
