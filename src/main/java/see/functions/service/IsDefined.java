package see.functions.service;


import com.google.common.base.Preconditions;
import see.functions.ContextCurriedFunction;
import see.functions.Function;

import java.util.List;
import java.util.Map;

public class IsDefined implements ContextCurriedFunction<Function<List<String>, Boolean>> {
    @Override
    public Function<List<String>, Boolean> apply(final Map<String, ?> context) {
        return new Function<List<String>, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                Preconditions.checkArgument(strings.size() == 1, "isDefined takes variable name");

                String variable = strings.get(0);
                
                return context.get(variable) != null;
            }
        };
    }

    @Override
    public String toString() {
        return "isDefined";
    }
}
