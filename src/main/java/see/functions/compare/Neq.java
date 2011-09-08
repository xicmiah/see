package see.functions.compare;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static see.functions.bool.BooleanCastHelper.fromBoolean;

public class Neq implements Function<List<BigDecimal>, BigDecimal> {
    private final Eq eq = new Eq();

    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Neq takes only two arguments");

        boolean value = !eq.unwrappedApply(input.get(0), input.get(1));

        return fromBoolean(value);
    }

    @Override
    public String toString() {
        return "!=";
    }
}
