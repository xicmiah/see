package see.parser.antlr.tree;

import org.antlr.runtime.ClassicToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.BaseTreeAdaptor;
import org.antlr.runtime.tree.Tree;
import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.parser.antlr.StdSeparatorsParser;
import see.parser.config.FunctionResolver;
import see.parser.numbers.NumberFactory;

import java.util.List;

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
            case StdSeparatorsParser.STRING:
                String text = token.getText();
                return new ConstantTreeNode<String>(token, text.substring(1, text.length()-1));
            case StdSeparatorsParser.NUMBER:
                return new ConstantTreeNode<Number>(token, numberFactory.getNumber(token.getText()));
            case StdSeparatorsParser.VARIABLE:
                return new VarTreeNode(token);
            case StdSeparatorsParser.UNAR:
                if ("+".equals(token.getText())){
                    return new TransitionNode<Object>(token);
                }else {
                    return new OperatorNode<Object, Object>(token,
                            functions.get(getUnarFunctionName(token.getText())));
                }
            case StdSeparatorsParser.ASSIGN:
            case StdSeparatorsParser.OPERATOR:
            case StdSeparatorsParser.IF_ST:
                return new OperatorNode<Object, Object>(token, getFunction(token.getText()));
            case StdSeparatorsParser.FUNCTION_CALL:
                return new SimpleFunctionNode<Object, Object>(token, getFunction(token.getText()));
            case StdSeparatorsParser.BLOCK:
                return new OperatorNode<Object, Object>(token, getFunction("seq"));
            case StdSeparatorsParser.RETURN_KW:
                return new TransitionNode<Object>(token);
            default:
                //todo think about using single dummy node instead of creating them
                return new DummyNode(token);
        }
    }

    private ContextCurriedFunction<Function<List<Object>, Object>> getFunction(String name) {
        ContextCurriedFunction<Function<List<Object>, Object>> functionContextCurriedFunction = functions.get(name);
        if (functionContextCurriedFunction == null){
            throw new RuntimeException("Function not found: "+name);
        }
        return functionContextCurriedFunction;
    }


    private String getUnarFunctionName(String text) {
        if ("!".equals(text)) {
            return "not";
        } else if ("-".equals(text)) {
            return "minus";
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
