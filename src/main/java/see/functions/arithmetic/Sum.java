package see.functions.arithmetic;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

public class Sum implements VarArgFunction<BigDecimal, BigDecimal> {
    @Override
    public BigDecimal apply(@Nonnull List<BigDecimal> input) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal value : input) {
            result = result.add(value);
        }
        return result;
    }

    @Override
    public String toString() {
        return "sum";
    }
}
