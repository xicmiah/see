package see.parser.config;

import see.evaluation.ValueProcessor;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;

public class GrammarConfiguration {
    private final FunctionResolver functions;
    private final NumberFactory numberFactory;
    private final ChainResolver properties;
    private final ValueProcessor valueProcessor;

    public GrammarConfiguration(FunctionResolver functions, NumberFactory numberFactory, ChainResolver properties, ValueProcessor processor) {
        this.functions = functions;
        this.numberFactory = numberFactory;
        this.properties = properties;
        this.valueProcessor = processor;
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

    public ValueProcessor getValueProcessor() {
        return valueProcessor;
    }
}
