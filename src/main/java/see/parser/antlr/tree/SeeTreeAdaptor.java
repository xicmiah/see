package see.parser.antlr.tree;

import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.BaseTreeAdaptor;
import org.antlr.runtime.tree.Tree;
import see.parser.antlr.SeeAntlrParser;
import see.parser.antlr.tree.*;
import see.parser.config.FunctionResolver;
import see.parser.numbers.NumberFactory;

/**
 * @author pavlov
 * @since 05.09.11
 */
public class SeeTreeAdaptor extends BaseTreeAdaptor {

    private final NumberFactory numberFactory;
    private final FunctionResolver functions;

    public SeeTreeAdaptor(NumberFactory numberFactory, FunctionResolver functions) {
        this.numberFactory = numberFactory;
        this.functions = functions;
    }

    @Override
    public Object create(Token payload) {
        if (payload == null) {
            return createNodeWithEmptyToken();
        } else {
            return createNodeForToken(payload);
        }
    }

    private Object createNodeWithEmptyToken() {
        return new OperatorNode<Object, Object>(null, functions.get("seq"));
    }

    private Object createNodeForToken(Token token) {
        switch (token.getType()) {
            case SeeAntlrParser.STRING:
                String text = token.getText();
                return new ConstantTreeNode<String>(token, text.substring(1, text.length()-1));
            case SeeAntlrParser.NUMBER:
                return new ConstantTreeNode<Number>(token, numberFactory.getNumber(token.getText()));
            case SeeAntlrParser.VARIABLE:
                return new VarTreeNode(token);
            case SeeAntlrParser.UNAR:
                return new OperatorNode<Object, Object>(token, functions.get(getUnarFunctionName(token.getText())));
            case SeeAntlrParser.ASSIGN:
            case SeeAntlrParser.OPERATOR:
            case SeeAntlrParser.IF_ST:
                return new OperatorNode<Object, Object>(token, functions.get(token.getText()));
            case SeeAntlrParser.FUNCTION_CALL:
                return new SimpleFunctionNode<Object, Object>(token, functions.get(token.getText()));
            case SeeAntlrParser.BLOCK:
                return new OperatorNode<Object, Object>(token, functions.get("seq"));
            default:
                //todo think about using single dummy node instead of creating them
                return new DummyNode(token);
        }
    }

    private String getUnarFunctionName(String text) {
        if ("!".equals(text)) {
            return "not";
        } else if ("-".equals(text)) {
            return "uminus";
        } else if ("+".equals(text)) {
            return "uplus";
        } else {
            throw new RuntimeException("unknown unar function " + text);
        }
    }

    @Override
    public Token createToken(int tokenType, String text) {
        return new ClassicToken(tokenType, text);
    }

    @Override
    public Token createToken(Token fromToken) {
        return new ClassicToken(fromToken);
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
        return ((SeeTreeNode) t).getTokenStartIndex();
    }

    @Override
    public int getTokenStopIndex(Object t) {
        return ((SeeTreeNode) t).getTokenStopIndex();
    }

    @Override
    public Object getParent(Object t) {
        return ((SeeTreeNode) t).getParent();
    }

    @Override
    public void setParent(Object t, Object parent) {
        Tree treeNode = (Tree) t;
        treeNode.setParent((Tree) parent);
    }

    @Override
    public int getChildIndex(Object t) {
        return ((Tree) t).getChildIndex();
    }

    @Override
    public void setChildIndex(Object t, int index) {
        ((Tree) t).setChildIndex(index);
    }

    @Override
    public void replaceChildren(Object parent, int startChildIndex, int stopChildIndex, Object t) {
        if (parent != null) {
            ((Tree) parent).replaceChildren(startChildIndex, stopChildIndex, t);
        }
    }
}
