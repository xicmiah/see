package see.parser.config;

import see.evaluator.ValueProcessor;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;

import java.util.List;

public class GrammarConfiguration {
    private final FunctionResolver functions;
    private final NumberFactory numberFactory;
    private final ChainResolver properties;
    private final List<ValueProcessor> valueProcessors;

    public GrammarConfiguration(FunctionResolver functions, NumberFactory numberFactory, ChainResolver properties, List<ValueProcessor> valueProcessors) {
        this.functions = functions;
        this.numberFactory = numberFactory;
        this.properties = properties;
        this.valueProcessors = valueProcessors;
    }

    public FunctionResolver getFunctions() {
        return functions;
    }

    public NumberFactory getNumberFactory() {
        return numberFactory;
    }

    public ChainResolver getChainResolver() {
        return properties;
    }

    public List<ValueProcessor> getValueProcessors() {
        return valueProcessors;
    }
}
