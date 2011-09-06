package see.functions.service;


import com.google.common.base.Preconditions;
import see.functions.ContextCurriedFunction;
import see.functions.Function;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static see.functions.bool.BooleanCastHelper.fromBoolean;

public class IsDefined implements ContextCurriedFunction<Function<List<String>, BigDecimal>> {
    @Override
    public Function<List<String>, BigDecimal> apply(final Map<String, Object> context) {
        return new Function<List<String>, BigDecimal>() {
            @Override
            public BigDecimal apply(List<String> strings) {
                Preconditions.checkArgument(strings.size() == 1, "isDefined takes variable name");

                String variable = strings.get(0);
                
                return fromBoolean(context.get(variable) != null);
            }
        };
    }

    @Override
    public String toString() {
        return "isDefined";
    }
}
