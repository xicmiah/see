package see.functions.arithmetic;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

public class Product implements VarArgFunction<BigDecimal, BigDecimal> {
    @Override
    public BigDecimal apply(@Nonnull List<BigDecimal> input) {
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
