package see.parser.config;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import see.evaluator.ValueProcessor;
import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.functions.PureFunction;
import see.functions.arithmetic.*;
import see.functions.bool.And;
import see.functions.bool.Not;
import see.functions.bool.Or;
import see.functions.common.Addition;
import see.functions.compare.*;
import see.functions.properties.GetProperty;
import see.functions.reactive.Bind;
import see.functions.reactive.MakeSignal;
import see.functions.service.*;
import see.functions.string.Concat;
import see.parser.numbers.BigDecimalFactory;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.properties.PartialResolver;
import see.properties.PropertyResolver;
import see.properties.impl.AggregatingResolver;
import see.properties.impl.MethodResolver;
import see.properties.impl.PropertyUtilsResolver;
import see.properties.impl.SingularChainResolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigBuilder {
    private Map<String, String> aliases;
    private Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions;
    
    private List<ValueProcessor> valueProcessors = ImmutableList.of();

    private NumberFactory numberFactory = new BigDecimalFactory();
    private ChainResolver propertyResolver = new SingularChainResolver(new AggregatingResolver(ImmutableList.<PartialResolver>of(new MethodResolver()), new PropertyUtilsResolver()));

    private ConfigBuilder(Map<String, String> aliases,
                          Map<String, ContextCurriedFunction<Function<List<Object>, Object>>> functions) {
        this.aliases = aliases;
        this.functions = functions;
    }

    public static ConfigBuilder defaultConfig() {
        final ConfigBuilder builder = ConfigBuilder.emptyConfig();
        builder.setNumberFactory(new BigDecimalFactory());

        addServiceFunctions(builder);
        addLogic(builder);
        addArithmetic(builder);
        addCompare(builder);
        addProperty(builder);
        addIteration(builder);
        addBindings(builder);
        addCommon(builder);
        addString(builder);
        return builder;
    }

    private static void addBindings(final ConfigBuilder builder) {
        builder.addAlias("<-", "bind");
        builder.addFunction("bind", new Bind());
        builder.addFunction("signal", new MakeSignal());
    }

    private static void addIteration(ConfigBuilder builder) {
        builder.addAlias("for", "iterate");
        builder.addFunction("iterate", new Iterate());
        builder.addPureFunction("while", new While());
    }

    private static void addProperty(ConfigBuilder builder) {
        builder.addAlias(".", "get");
        builder.addPureFunction("get", new GetProperty());
    }

    private static void addCompare(ConfigBuilder builder) {
        builder.addPureFunction("==", new Eq());
        builder.addPureFunction("!=", new Neq());
        builder.addPureFunction(">", new Gt());
        builder.addPureFunction(">=", new Geq());
        builder.addPureFunction("<", new Lt());
        builder.addPureFunction("<=", new Leq());
    }

    private static void addCommon(ConfigBuilder builder) {
        builder.addAlias("+", "addition");
        builder.addPureFunction("addition", new Addition());
    }

    private static void addString(ConfigBuilder builder){
        builder.addPureFunction("concat", new Concat());
    }

    private static void addArithmetic(final ConfigBuilder builder) {
        builder.addAlias("-", "minus");
        builder.addAlias("*", "product");
        builder.addAlias("/", "divide");
        builder.addAlias("^", "pow");

        builder.addPureFunction("min", new Min<BigDecimal>());
        builder.addPureFunction("max", new Max<BigDecimal>());
        builder.addPureFunction("sum", new Sum());
        builder.addPureFunction("minus", new Minus());
        builder.addPureFunction("product", new Product());
        builder.addPureFunction("divide", new Divide(new Supplier<MathContext>() {
            @Override
            public MathContext get() {
                return ((BigDecimalFactory) builder.numberFactory).getMathContext();
            }
        })); // Math context is passed by-name, will adapt to setNumberFactory() calls on builder
        builder.addPureFunction("pow", new Power());
    }

    private static void addLogic(ConfigBuilder builder) {
        builder.addAlias("!", "not");
        builder.addAlias("&&", "and");
        builder.addAlias("||", "or");

        builder.addPureFunction("not", new Not());
        builder.addPureFunction("and", new And());
        builder.addPureFunction("or", new Or());
    }

    private static void addServiceFunctions(ConfigBuilder builder) {
        builder.addAlias(";", "seq");
        builder.addAlias("=", "assign");
        builder.addAlias("v=", "vSet");

        builder.addFunction("seq", wrap(new Sequence<Object>()));
        builder.addPureFunction("assign", new Assign<Object>());
        builder.addFunction("vSet", new VarAsSettable());
        builder.addFunction("isDefined", new IsDefined());
        builder.addPureFunction("if", new If<Object>());
        builder.addPureFunction("apply", new Apply());
    }

    public static ConfigBuilder emptyConfig() {
        return new ConfigBuilder(
                new HashMap<String, String>(),
                new HashMap<String, ContextCurriedFunction<Function<List<Object>, Object>>>()
        );
    }

    public ConfigBuilder addAlias(String name, String alias) {
        aliases.put(name, alias);
        return this;
    }

    /**
     * Add supplied function to registry.
     * Function should accept context via currying
     *
     * @param name     function name
     * @param function function to add
     * @param <T>      function argument type
     * @param <R>      function result type
     * @return this instance
     */
    public <T, R> ConfigBuilder addFunction(String name, ContextCurriedFunction<Function<List<T>, R>> function) {
        functions.put(name, wrap(function));
        return this;
    }

    /**
     * Add supplied pure function to registry.
     *
     * @param name     function name
     * @param function function to add
     * @param <T>      function argument type
     * @param <R>      function result type
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

    /**
     * Set custom property resolver (for one property).
     * 
     * @param propertyResolver new property resolver
     * @return this instance
     */
    public ConfigBuilder setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = new SingularChainResolver(propertyResolver);
        return this;
    }

    /**
     * Set custom resolver for property chains.
     * @param chainResolver new property resolver
     * @return this instance
     */
    public ConfigBuilder setChainResolver(ChainResolver chainResolver) {
        this.propertyResolver = chainResolver;
        return this;
    }

    /**
     * Set custom value processors
     * @param valueProcessors custom value processors
     * @return this instance
     */
    public ConfigBuilder setValueProcessors(List<ValueProcessor> valueProcessors) {
        this.valueProcessors = valueProcessors;
        return this;
    }

    public GrammarConfiguration build() {
        return new GrammarConfiguration(
                new FunctionResolver(functions, aliases),
                numberFactory,
                propertyResolver,
                ImmutableList.copyOf(valueProcessors)
        );
    }

    /**
     * Cast supplied function to type
     *
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
