package see.parser.config;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import see.evaluation.ValueProcessor;
import see.evaluation.processors.NumberLifter;
import see.functions.ContextCurriedFunction;
import see.functions.PureFunction;
import see.functions.VarArgFunction;
import see.parser.numbers.BigDecimalFactory;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.properties.PropertyResolver;
import see.properties.impl.MethodResolver;
import see.properties.impl.PropertyUtilsResolver;
import see.properties.impl.SingularChainResolver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.collect.ImmutableList.of;
import static see.evaluation.processors.AggregatingProcessor.concat;
import static see.properties.PropertyResolvers.aggregate;
import static see.properties.PropertyResolvers.universalResolver;

public class ConfigBuilder {
    private Map<String, String> aliases;
    private Map<String, ContextCurriedFunction<Object, Object>> functions;
    
    private List<? extends ValueProcessor> valueProcessors = of(new NumberLifter(getNumberFactoryReference()));

    private AtomicReference<NumberFactory> numberFactory = new AtomicReference<NumberFactory>(new BigDecimalFactory());
    private ChainResolver propertyResolver = new SingularChainResolver(aggregate(of(new MethodResolver(), universalResolver(new PropertyUtilsResolver()))));

    private ConfigBuilder(Map<String, String> aliases,
                          Map<String, ContextCurriedFunction<Object, Object>> functions) {
        this.aliases = aliases;
        this.functions = functions;
    }

    public static ConfigBuilder defaultConfig() {
        return DefaultConfig.defaultConfig();
    }

    public static ConfigBuilder emptyConfig() {
        Map<String, String> aliases = Maps.newHashMap();
        Map<String, ContextCurriedFunction<Object, Object>> functions = Maps.newHashMap();
        return new ConfigBuilder(aliases, functions);
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
    public <T, R> ConfigBuilder addFunction(String name, ContextCurriedFunction<T, R> function) {
        functions.put(name, wrap(function));
        return this;
    }

    /**
     * Add supplied pure function to registry.
     *
     * @param name     function name
     * @param function function to add
     * @param <A>      function argument type
     * @param <R>      function result type
     * @return this instance
     */
    public <A, R> ConfigBuilder addPureFunction(String name, VarArgFunction<A, R> function) {
        functions.put(name, wrap(function));
        return this;
    }

    public ConfigBuilder setNumberFactory(NumberFactory numberFactory) {
        this.numberFactory.set(numberFactory);
        return this;
    }

    public Supplier<NumberFactory> getNumberFactoryReference() {
        return new Supplier<NumberFactory>() {
            @Override
            public NumberFactory get() {
                return numberFactory.get();
            }
        };
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
                numberFactory.get(),
                propertyResolver,
                concat(valueProcessors)
        );
    }

    /**
     * Cast supplied function to type
     *
     * @param function function to wrap
     * @return wrapped function
     */
    @SuppressWarnings("unchecked")
    private static <Arg, Result> ContextCurriedFunction<Object, Object> wrap(final ContextCurriedFunction<Arg, Result> function) {
        // Intentional raw type usage
        return (ContextCurriedFunction) function;
    }

    @SuppressWarnings("unchecked")
    private static <Arg, Result> ContextCurriedFunction<Object, Object> wrap(final VarArgFunction<Arg, Result> function) {
        // Intentional raw type usage
        return new PureFunction(function);
    }
}
