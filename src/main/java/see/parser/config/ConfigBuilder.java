package see.parser.config;

import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.functions.PureFunction;
import see.functions.arithmetic.*;
import see.functions.bool.And;
import see.functions.bool.Not;
import see.functions.bool.Or;
import see.functions.compare.*;
import see.functions.service.Assign;
import see.functions.service.If;
import see.functions.service.IsDefined;
import see.functions.service.Sequence;
import see.parser.numbers.BigDecimalFactory;
import see.parser.numbers.NumberFactory;

import java.math.BigDecimal;
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

        aliases.put("!", "not");
        aliases.put("&&", "and");
        aliases.put("||", "or");

        aliases.put("+", "sum");
        aliases.put("-", "minus");
        aliases.put("*", "product");
        aliases.put("/", "divide");
        aliases.put("^", "pow");

        Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions =
                new HashMap<String, ContextCurriedFunction<Function<List<Object>, Object>>>();

        functions.put("seq", wrap(new Sequence<Object>()));
        functions.put("assign", wrap(new Assign<Object>()));
        functions.put("if", wrap(new If<Object>()));
        functions.put("isDefined", wrap(new IsDefined()));

        functions.put("not", wrap(new Not()));
        functions.put("and", wrap(new And()));
        functions.put("or", wrap(new Or()));

        functions.put("==", wrap(new Eq()));
        functions.put("!=", wrap(new Neq()));
        functions.put(">", wrap(new Gt()));
        functions.put(">=", wrap(new Geq()));
        functions.put("<", wrap(new Lt()));
        functions.put("<=", wrap(new Leq()));

        functions.put("min", wrap(new Min<BigDecimal>()));
        functions.put("max", wrap(new Max<BigDecimal>()));

        functions.put("sum", wrap(new Sum()));
        functions.put("minus", wrap(new Minus()));
        functions.put("product", wrap(new Product()));
        functions.put("divide", wrap(new Divide()));
        functions.put("pow", wrap(new Power()));

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

    /**
     * Add supplied function to registry.
     * Function should accept context via currying
     * @param name function name
     * @param function function to add
     * @param <T> function argument type
     * @param <R> function result type
     * @return this instance
     */
    public <T, R> ConfigBuilder addFunction(String name, ContextCurriedFunction<Function<List<T>, R>> function) {
        functions.put(name, wrap(function));
        return this;
    }

    /**
     * Add supplied pure function to registry.
     * @param name function name
     * @param function function to add
     * @param <T> function argument type
     * @param <R> function result type
     * @return this instance
     */
    public <T, R> ConfigBuilder addPureFunction(String name, Function<List<T>, R> function) {
        functions.put(name, wrap(function));
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
     * Cast supplied function to type
     * @param function function to wrap
     * @return wrapped function
     */
    @SuppressWarnings("unchecked")
    private static <Arg, Result> ContextCurriedFunction<Function<List<Object>, Object>> wrap(final ContextCurriedFunction<Function<List<Arg>, Result>> function) {
        // Intentional raw type usage
        return (ContextCurriedFunction) function;
    }

    @SuppressWarnings("unchecked")
    private static <Arg, Result> ContextCurriedFunction<Function<List<Object>, Object>> wrap(final Function<List<Arg>, Result> function) {
        // Intentional raw type usage
        return new PureFunction(function);
    }
}
