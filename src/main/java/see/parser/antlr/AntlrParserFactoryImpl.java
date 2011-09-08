package see.parser.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import see.parser.config.GrammarConfiguration;

/**
 * @author pavlov
 * @since 08.09.11
 */
public class AntlrParserFactoryImpl implements AntlrParserFactory {

    @Override
    public AbstractAntlrGrammarParser getParser(GrammarConfiguration gc, String expression) {
        if (gc.getNumberFactory().getDecimalSeparator().equals(',')){
            AlternativeSeparatorsLexer lexer = new AlternativeSeparatorsLexer(new ANTLRStringStream(expression));
            AlternativeSeparatorsParser parser = new AlternativeSeparatorsParser(new CommonTokenStream(lexer));
            return parser;
        }else if (gc.getNumberFactory().getDecimalSeparator().equals('.')) {
            StdSeparatorsLexer lexer = new StdSeparatorsLexer(new ANTLRStringStream(expression));
            StdSeparatorsParser parser = new StdSeparatorsParser(new CommonTokenStream(lexer));
            return parser;
        }else{
            throw new RuntimeException("can't find suitable parser");
        }
    }
}
