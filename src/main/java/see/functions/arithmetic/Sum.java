package see.functions.arithmetic;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

public class Sum implements VarArgFunction<Comparable, Comparable> {
    @Override
    public Comparable apply(@Nonnull List<Comparable> input) {
        BigDecimal result = BigDecimal.ZERO;
        for (Comparable value : input) {
            BigDecimal decValue=(BigDecimal) value;
            result = result.add(decValue);
        }
        return result;
    }

    @Override
    public String toString() {
        return "sum";
    }
}
