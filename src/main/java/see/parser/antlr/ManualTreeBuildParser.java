package see.parser.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import see.exceptions.ParseException;
import see.parser.Parser;
import see.parser.config.GrammarConfiguration;
import see.tree.Node;

/**
 * @author pavlov
 * @since 09.09.11
 */
public class ManualTreeBuildParser<T> implements Parser<T> {

    private final SeeNodesFactory nodesFactory;
    private final GrammarConfiguration gc;
    private final boolean parseMultipleExpressions;

    public ManualTreeBuildParser(GrammarConfiguration gc, boolean parseMultipleExpressions) {
        this.gc = gc;
        this.parseMultipleExpressions = parseMultipleExpressions;
        nodesFactory = new NodesFactoryImpl(gc);
    }

    @Override
    public Node<T> parse(String input) throws ParseException {
        AbstractAntlrGrammarParser parser = createParser(input);
        return runParser(parser);
    }

    private AbstractAntlrGrammarParser createParser(String expression) {
        Character decimalSeparator = gc.getNumberFactory().getDecimalSeparator();
        if (decimalSeparator.equals('.')) {
            Lexer lexer = new ManualStdSeparatorsLexer(new ANTLRStringStream(expression));
            ManualStdSeparatorsParser parser = new ManualStdSeparatorsParser(new CommonTokenStream(lexer));
            parser.gCommonManualNodeBuilder.setNodesFactory(nodesFactory);

            return parser;
        } else if (decimalSeparator.equals(',')) {
            Lexer lexer = new ManualAlternativeSeparatorsLexer(new ANTLRStringStream(expression));
            ManualAlternativeSeparatorsParser parser = new ManualAlternativeSeparatorsParser(new CommonTokenStream(lexer));
            parser.gCommonManualNodeBuilder.setNodesFactory(nodesFactory);

            return parser;
        }else{
            throw new RuntimeException("Can't find suitable grammar parser");
        }
    }

    @SuppressWarnings({"unchecked"})
    private Node<T> runParser(AbstractAntlrGrammarParser parser) {
        try {
            if (parseMultipleExpressions) {
                return (Node<T>) parser.multipleExpressions();
            } else {
                return (Node<T>) parser.singleExpression();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
