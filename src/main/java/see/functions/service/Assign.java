package see.functions.service;

import see.functions.ContextCurriedFunction;
import see.functions.Function;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Assignment.
 *
 * Updates context and returns assigned value
 * @param <T>
 */
public class Assign<T> implements ContextCurriedFunction<Function<List<Object>, T>> {
    @Override
    public Function<List<Object>, T> apply(final Map<String, Object> context) {
        return new Function<List<Object>, T>() {
            @Override
            public T apply(List<Object> args) {
                checkArgument(args.size() == 2, "Assign takes variable name and value");

                String variable = (String) args.get(0);
                @SuppressWarnings("unchecked") T value = (T) args.get(1);

                context.put(variable, value);

                return value;
            }
        };
    }

    @Override
    public String toString() {
        return "assign";
    }
}
