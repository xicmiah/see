package see.parser.antlr;

import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.tree.BaseTree;

/**
 * @author pavlov
 * @since 05.09.11
 */
public abstract class SeeTreeNode extends BaseTree {

    private final ClassicToken token;

    private int childIndex;

    protected SeeTreeNode(ClassicToken token) {
        this.token = token;
    }

    public ClassicToken getToken() {
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
