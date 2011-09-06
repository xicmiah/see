package see.functions.bool;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static see.functions.bool.BooleanCastHelper.fromBoolean;
import static see.functions.bool.BooleanCastHelper.toBoolean;

/**
 * Logical Or function. Short-circuits.
 */
public class Or implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        for (BigDecimal value : input) {
            if (toBoolean(value)) return fromBoolean(true);
        }
        return fromBoolean(false);
    }

    @Override
    public String toString() {
        return "or";
    }
}
