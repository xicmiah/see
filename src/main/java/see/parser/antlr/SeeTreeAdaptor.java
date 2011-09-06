package see.parser.antlr;

import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.BaseTreeAdaptor;
import org.antlr.runtime.tree.Tree;

/**
 * @author pavlov
 * @since 05.09.11
 */
public class SeeTreeAdaptor extends BaseTreeAdaptor {

    @Override
    public Token createToken(int tokenType, String text) {
        return new ClassicToken(tokenType, text);
    }

    @Override
    public Token createToken(Token fromToken) {
        return new ClassicToken(fromToken);
    }

    @Override
    public Object create(Token payload) {
        return null;
    }

    @Override
    public Object dupNode(Object treeNode) {
        return ((Tree) treeNode).dupNode();
    }

    @Override
    public Token getToken(Object t) {
        return ((SeeTreeNode) t).getToken();
    }

    @Override
    public void setTokenBoundaries(Object t, Token startToken, Token stopToken) {
    }

    @Override
    public int getTokenStartIndex(Object t) {
        return ((SeeTreeNode)t).getTokenStartIndex();
    }

    @Override
    public int getTokenStopIndex(Object t) {
        return ((SeeTreeNode)t).getTokenStopIndex();
    }

    @Override
    public Object getParent(Object t) {
        return ((SeeTreeNode)t).getParent();
    }

    @Override
    public void setParent(Object t, Object parent) {
        Tree treeNode = (Tree) t;
        treeNode.setParent((Tree) parent);
    }

    @Override
    public int getChildIndex(Object t) {
        return ((Tree)t).getChildIndex();
    }

    @Override
    public void setChildIndex(Object t, int index) {
        ((Tree)t).setChildIndex(index);
    }

    @Override
    public void replaceChildren(Object parent, int startChildIndex, int stopChildIndex, Object t) {
        if ( parent!=null ) {
			((Tree)parent).replaceChildren(startChildIndex, stopChildIndex, t);
		}
    }
}
