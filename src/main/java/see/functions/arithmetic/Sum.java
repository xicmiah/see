package see.functions.arithmetic;

import com.google.common.base.Function;

import java.math.BigDecimal;
import java.util.List;

public class Sum implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
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
