package see.parser.antlr;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.TreeAdaptor;
import see.exceptions.ParseException;
import see.tree.Node;

/**
 * @author pavlov
 * @since 08.09.11
 */
public abstract class AbstractAntlrGrammarParser extends Parser {

    protected AbstractAntlrGrammarParser(TokenStream input) {
        super(input);
    }

    protected AbstractAntlrGrammarParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public <T> Node<T> singleExpression() throws RecognitionException {
        throw new UnsupportedOperationException();
    }

    public <T> Node<T> multipleExpressions() throws RecognitionException {
        throw new UnsupportedOperationException();
    }

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        throw new UnsupportedOperationException();
    }


    public Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet allow) throws MismatchedTokenException {
        throw new MismatchedTokenException(ttype, input);
    }

    @Override
    public void reportError(RecognitionException e) {
        throw new ParseException(ExceptionConverter.createParserErrorDescription(e, this, state));
    }
}
