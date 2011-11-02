package see.functions.bool;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static see.functions.bool.BooleanCastHelper.fromBoolean;
import static see.functions.bool.BooleanCastHelper.toBoolean;

/**
 * Logical And function. Short-circuits.
 */
public class And implements Function<List<Boolean>, Boolean> {
    @Override
    public Boolean apply(List<Boolean> input) {
        for (Boolean value : input) {
            if (!value) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "and";
    }
}
