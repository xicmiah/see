package see.functions.compare;

import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static see.functions.bool.BooleanCastHelper.fromBoolean;

public class Eq implements Function<List<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(List<BigDecimal> input) {
        checkArgument(input.size() == 2, "Eq takes only two arguments");

        boolean value = input.get(0).compareTo(input.get(1)) == 0;

        return fromBoolean(value);
    }

    @Override
    public String toString() {
        return "==";
    }
}
