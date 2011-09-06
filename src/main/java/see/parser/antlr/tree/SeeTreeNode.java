package see.parser.antlr.tree;

import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.BaseTree;
import see.tree.Node;

/**
 * @author pavlov
 * @since 05.09.11
 */
public abstract class SeeTreeNode<T> extends BaseTree implements Node<T> {

    private final Token token;

    private int childIndex;

    protected SeeTreeNode(Token token) {
        this.token = token;
    }

    @Override
    public boolean isNil() {
        return token == null;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public int getType() {
        return token.getType();
    }

    @Override
    public String getText() {
        return token.getText();
    }

    @Override
    public int getTokenStartIndex() {
        return token.getTokenIndex();
    }

    @Override
    public void setTokenStartIndex(int index) {
    }

    @Override
    public int getTokenStopIndex() {
        int tokenLength = 0;
        if (token.getText() != null) {
            tokenLength = token.getText().length();
        }

        return token.getTokenIndex() + tokenLength;
    }

    @Override
    public void setTokenStopIndex(int index) {
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }
}
