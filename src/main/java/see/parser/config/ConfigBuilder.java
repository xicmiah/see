package see.parser.config;

import com.google.common.base.Function;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigBuilder {
    private Map<String, String> aliases;
    private Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions;
    private NumberFactory numberFactory;

    private ConfigBuilder(Map<String, String> aliases,
                          Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions,
                          NumberFactory numberFactory) {
        this.aliases = aliases;
        this.functions = functions;
        this.numberFactory = numberFactory;
    }

    public static ConfigBuilder defaultConfig() {
        Map<String, String> aliases = new HashMap<String, String>();
        aliases.put(";", "seq");
        aliases.put("=", "assign");
        aliases.put("+", "sum");
        aliases.put("-", "minus");
        aliases.put("*", "product");
        aliases.put("/", "divide");
        
        Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions =
                new HashMap<String, ContextCurriedFunction<Function<List<Object>, Object>>>();
        functions.put("seq", wrap(new Sequence<Object>()));
        functions.put("assign", wrap(new Assign<Object>()));
        functions.put("if", wrap(new If<Object>()));
        functions.put("isDefined", wrap(new IsDefined()));
        
        functions.put("sum", wrap(new Sum()));
        functions.put("minus", wrap(new Minus()));
        functions.put("product", wrap(new Product()));
        functions.put("divide", wrap(new Divide()));
        return new ConfigBuilder(aliases, functions, new BigDecimalFactory());
    }

    public static ConfigBuilder emptyConfig() {
        return new ConfigBuilder(new HashMap<String, String>(),
                new HashMap<String, ContextCurriedFunction<Function<List<Object>, Object>>>(),
                new BigDecimalFactory());
    }

    public ConfigBuilder addAlias(String name, String alias) {
        aliases.put(name, alias);
        return this;
    }

    public ConfigBuilder addFunction(String name, ContextCurriedFunction<Function<List<Object>, Object>> function) {
        functions.put(name, function);
        return this;
    }

    public ConfigBuilder setNumberFactory(NumberFactory numberFactory) {
        this.numberFactory = numberFactory;
        return this;
    }


    public GrammarConfiguration build() {
        return new GrammarConfiguration(new FunctionResolver(functions, aliases), numberFactory);
    }

    /**
     * Cast supplied function to UntypedFunction
     * @param function function to wrap
     * @param <Arg> function argument type
     * @param <Result> function result type
     * @return wrapped function
     */
    private static <Arg, Result> UntypedFunction wrap(final ContextCurriedFunction<Function<List<Arg>, Result>> function) {
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

    private static <Arg, Result> UntypedFunction wrap(final Function<List<Arg>, Result> function) {
        return wrap(new PureFunction<Function<List<Arg>, Result>>(function));
    }

    private static interface UntypedFunction extends ContextCurriedFunction<Function<List<Object>, Object>> {}

}
