package see.parser.antlr;

import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.TreeAdaptor;
import see.parser.antlr.tree.SeeTreeNode;
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

    public <T> SeeTreeNode<T> singleExpression() throws Exception{
        throw new UnsupportedOperationException();
    }

    public <T> SeeTreeNode<T> multipleExpressions() throws Exception{
        throw new UnsupportedOperationException();
    }

    public void setTreeAdaptor(TreeAdaptor adaptor){
        throw new UnsupportedOperationException();
    }


}
