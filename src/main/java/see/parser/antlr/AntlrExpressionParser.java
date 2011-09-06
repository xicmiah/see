package see.parser.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import see.exceptions.ParseException;
import see.parser.Parser;
import see.parser.antlr.tree.SeeTreeAdaptor;
import see.parser.antlr.tree.SeeTreeNode;
import see.tree.Node;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class AntlrExpressionParser<T> implements Parser<T> {

    private boolean parseMultipleExpressions;

    private SeeTreeAdaptor adaptor;

    @Override
    public Node<T> parse(String input) throws ParseException {
        ANTLRStringStream stringStream = new ANTLRStringStream(input);
        SeeAntlrLexer lexer = new SeeAntlrLexer(stringStream);
        SeeAntlrParser parser = new SeeAntlrParser(new CommonTokenStream(lexer));

        parser.setTreeAdaptor(adaptor);

        return runParser(parser);
    }

    @SuppressWarnings({"unchecked"})
    private SeeTreeNode<T> runParser(SeeAntlrParser parser) {
        try {
            if (parseMultipleExpressions){
                SeeAntlrParser.calculationExpression_return parseResult = parser.calculationExpression();
                return (SeeTreeNode<T>) parseResult.getTree();
            }else{
                SeeAntlrParser.conditionalExpression_return parserResult = parser.conditionalExpression();
                return (SeeTreeNode<T>) parserResult.getTree();
            }
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParseMultipleExpressions(boolean parseMultipleExpressions) {
        this.parseMultipleExpressions = parseMultipleExpressions;
    }

    public void setAdaptor(SeeTreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
}
