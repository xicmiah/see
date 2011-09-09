package see.parser.antlr;

import org.antlr.runtime.Token;
import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.parser.config.GrammarConfiguration;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.VarNode;
import see.tree.immutable.ImmutableConstNode;
import see.tree.immutable.ImmutableFunctionNode;
import see.tree.immutable.ImmutableVarNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author pavlov
 * @since 09.09.11
 */
public class NodesFactoryImpl implements SeeNodesFactory{

    private GrammarConfiguration gc;

    public NodesFactoryImpl(GrammarConfiguration gc) {
        this.gc = gc;
    }

    @Override
    public ConstNode<Object> createStringNode(Token stringToken) {
        String string = stringToken.getText();
        String stringWithouQuotes = string.substring(1, string.length() - 1);
        return new ImmutableConstNode<Object>(stringWithouQuotes);
    }

    @Override
    public ConstNode<Object> createNumberNode(Token number) {
        return new ImmutableConstNode<Object>(gc.getNumberFactory().getNumber(number.getText()));
    }

    @Override
    public VarNode<Object> createVarNode(Token varName) {
        return new ImmutableVarNode<Object>(varName.getText());
    }

    @Override
    public FunctionNode<Object, Object> createFunctionNode(Token functionName, List<Node<Object>> params) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunctionOrThrow(functionName);
        if (params == null){
            return new ImmutableFunctionNode<Object, Object>(func);
        }else{
            return new ImmutableFunctionNode<Object, Object>(func, params);
        }
    }

    private ContextCurriedFunction<Function<List<Object>, Object>> getFunctionOrThrow(Token functionName) {
        return getFunctionOrThrow(functionName.getText());
    }

    private ContextCurriedFunction<Function<List<Object>,Object>> getFunctionOrThrow(String functionName){
        ContextCurriedFunction<Function<List<Object>, Object>> func = gc.getFunctions().get(functionName);
        if (func == null){
            throw new RuntimeException("Function not found: "+functionName);
        }
        return func;
    }

    @Override
    public FunctionNode<Object, Object> createOperatorNode(Token functionName, Node<Object>... params) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunctionOrThrow(functionName);
        return new ImmutableFunctionNode<Object, Object>(func, Arrays.asList(params));
    }

    @Override
    public FunctionNode<Object, Object> createIfNode(Token ifToken, Node<Object> condition, Node<Object> thenNode, Node<Object> elseNode) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunctionOrThrow(ifToken);

        List<Node<Object>> ifArgs = new ArrayList<Node<Object>>(3);
        ifArgs.add(0,condition);
        if (thenNode != null){
            ifArgs.add(1, thenNode);
        }
        if (elseNode != null){
            ifArgs.add(2, elseNode);
        }

        return new ImmutableFunctionNode<Object, Object>(func, ifArgs);
    }

    @Override
    public FunctionNode<Object, Object> createSequence(List<Node<Object>> nodes) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunctionOrThrow("seq");
        return new ImmutableFunctionNode<Object, Object>(func, nodes);
    }

    @Override
    public Node<Object> createUndefinedNode() {
        return new ImmutableConstNode<Object>(null);
    }
}
