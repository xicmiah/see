package see.functions.bool;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static see.functions.bool.BooleanCastHelper.fromBoolean;
import static see.functions.bool.BooleanCastHelper.toBoolean;

/**
 * Logical Or function. Short-circuits.
 */
public class Or implements Function<List<Boolean>, Boolean> {
    @Override
    public Boolean apply(List<Boolean> input) {
        for (Boolean value : input) {
            if (value) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "or";
    }
}
