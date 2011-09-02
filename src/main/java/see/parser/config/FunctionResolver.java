package see.parser.config;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import see.functions.ContextCurriedFunction;
import see.functions.PureFunction;
import see.functions.arithmetic.Divide;
import see.functions.arithmetic.Minus;
import see.functions.arithmetic.Product;
import see.functions.arithmetic.Sum;
import see.functions.service.Assign;
import see.functions.service.If;
import see.functions.service.IsDefined;
import see.functions.service.Sequence;

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
            .put("if", wrap(new If<Object>()))
            .put("isDefined", wrap(new IsDefined()))

            .put("sum", wrap(new Sum()))
            .put("minus", wrap(new Minus()))
            .put("product", wrap(new Product()))
            .put("divide", wrap(new Divide()))
            .build();

    /**
     * Get function by name
     * @param name function name
     * @return corresponding function
     */
    public ContextCurriedFunction<Function<List<Object>, Object>> get(String name) {
        if (aliases.containsKey(name)) {
            return functions.get(aliases.get(name));
        } else {
            return functions.get(name);
        }
    }

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
     * A curried untyped function.
     * Serves as shortcut for long generic type declaration
     */
    private static interface UntypedFunction extends ContextCurriedFunction<Function<List<Object>, Object>> {}
}
