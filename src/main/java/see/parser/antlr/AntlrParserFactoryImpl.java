package see.parser.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;
import see.parser.config.GrammarConfiguration;

/**
 * @author pavlov
 * @since 08.09.11
 */
public class AntlrParserFactoryImpl implements AntlrParserFactory {

    private AbstractAntlrGrammarParser parser;

    private Lexer lexer;

    @Override
    public AbstractAntlrGrammarParser getParser(GrammarConfiguration gc, String expression) {
//        if (parser == null){
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
//        }else{
//            lexer.setCharStream(new ANTLRStringStream(expression));
//            parser.reset();
//            return parser;
//        }
    }
}
