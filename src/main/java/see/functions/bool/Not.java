package see.functions.bool;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static see.functions.bool.BooleanCastHelper.fromBoolean;
import static see.functions.bool.BooleanCastHelper.toBoolean;

public class Not implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        checkArgument(input.size() == 1, "Not takes only one argument");
        checkNotNull(input.get(0));

        return fromBoolean(!toBoolean(input.get(0)));
    }
    @Override
    public String toString() {
        return "not";
    }
}