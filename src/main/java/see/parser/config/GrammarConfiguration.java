package see.parser.config;

import see.parser.numbers.NumberFactory;

public class GrammarConfiguration {
    private final FunctionResolver functions;
    private final NumberFactory numberFactory;

    public GrammarConfiguration(FunctionResolver functions, NumberFactory numberFactory) {
        this.functions = functions;
        this.numberFactory = numberFactory;
    }

    public FunctionResolver getFunctions() {
        return functions;
    }

    public NumberFactory getNumberFactory() {
        return numberFactory;
    }
}
