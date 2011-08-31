package see.parser.grammar;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import see.functions.*;

import java.util.List;
import java.util.Map;

public class FunctionResolver {
    private final ImmutableMap<String, String> aliases = ImmutableMap.<String, String>builder()
            .put(";", "seq")
            .put("=", "assign")
            .put("+", "sum")
            .put("-", "minus")
            .put("*", "product")
            .put("/", "divide")
            .build();

    private final ImmutableMap<String, UntypedFunction> functions = ImmutableMap.<String, UntypedFunction>builder()
            .put("seq", wrap(new Sequence<Object>()))
            .put("assign", wrap(new Assign<Object>()))
            .put("isDefined", wrap(new IsDefined()))
            .build();

    /**
     * Cast supplied function to UntypedFunction
     * @param function function to wrap
     * @param <Arg> function argument type
     * @param <Result> function result type
     * @return wrapped function
     */
    private <Arg, Result> UntypedFunction wrap(final ContextCurriedFunction<Function<List<Arg>, Result>> function) {
        return new UntypedFunction() {
            @Override
            public String toString() {
                return function.toString();
            }

            @Override
            public Function<List<Object>, Object> apply(final Map<String, Object> context) {
                return new Function<List<Object>, Object>() {
                    @Override
                    public Object apply(List<Object> input) {
                        return function.apply(context).apply((List<Arg>) input);
                    }
                };
            }
        };
    }

    private <Arg, Result> UntypedFunction wrap(final Function<List<Arg>, Result> function) {
        return wrap(new PureFunction<Function<List<Arg>, Result>>(function));
    }

    /**
     * Get function by name
     * @param name function name
     * @return corresponding function
     */
    public UntypedFunction get(String name) {
        if (aliases.containsKey(name)) {
            return functions.get(aliases.get(name));
        } else {
            return functions.get(name);
        }
    }

}
