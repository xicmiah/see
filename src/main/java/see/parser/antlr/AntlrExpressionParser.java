package see.parser.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import see.exceptions.ParseException;
import see.parser.Parser;
import see.parser.antlr.tree.SeeTreeAdaptor;
import see.parser.antlr.tree.SeeTreeNode;
import see.parser.config.GrammarConfiguration;
import see.tree.Node;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class AntlrExpressionParser<T> implements Parser<T> {

    private boolean parseMultipleExpressions;

    private SeeTreeAdaptor adaptor;

    private AntlrParserFactory parserFactory;
    private GrammarConfiguration gc;

    public AntlrExpressionParser(GrammarConfiguration configuration){
        gc = configuration;
        adaptor = new SeeTreeAdaptor(configuration.getNumberFactory(), configuration.getFunctions());

    }

    @Override
    public Node<T> parse(String input) throws ParseException {
        AbstractAntlrGrammarParser parser = parserFactory.getParser(gc, input);
        parser.setTreeAdaptor(adaptor);

        return runParser(parser);
    }

    @SuppressWarnings({"unchecked"})
    private Node<T> runParser(AbstractAntlrGrammarParser parser) {
        try {
            if (parseMultipleExpressions){
                return parser.multipleExpressions();
            }else{
                return parser.singleExpression();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setParseMultipleExpressions(boolean parseMultipleExpressions) {
        this.parseMultipleExpressions = parseMultipleExpressions;
    }

    public void setParserFactory(AntlrParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }
}
