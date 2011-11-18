package see.functions.arithmetic;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

public class Sum implements Function<List<Comparable>, Comparable> {
    @Override
    public Comparable apply(List<Comparable> input) {
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
