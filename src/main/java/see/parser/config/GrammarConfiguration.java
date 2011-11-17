package see.parser.config;

import see.functions.properties.ChainResolver;
import see.parser.numbers.NumberFactory;

public class GrammarConfiguration {
    private final FunctionResolver functions;
    private final NumberFactory numberFactory;
    private final ChainResolver properties;

    public GrammarConfiguration(FunctionResolver functions, NumberFactory numberFactory, ChainResolver properties) {
        this.functions = functions;
        this.numberFactory = numberFactory;
        this.properties = properties;
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
}
