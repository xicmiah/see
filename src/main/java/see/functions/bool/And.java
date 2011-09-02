package see.functions.bool;

import com.google.common.base.Function;

import java.math.BigDecimal;
import java.util.List;

import static see.functions.bool.BooleanCastHelper.fromBoolean;
import static see.functions.bool.BooleanCastHelper.toBoolean;

/**
 * Logical And function. Short-circuits.
 */
public class And implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        for (BigDecimal value : input) {
            if (!toBoolean(value)) return fromBoolean(false);
        }
        return fromBoolean(true);
    }

    @Override
    public String toString() {
        return "and";
    }
}
