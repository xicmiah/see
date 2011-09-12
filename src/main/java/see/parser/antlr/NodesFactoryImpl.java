package see.parser.antlr;

import org.antlr.runtime.Token;
import see.exceptions.FunctionNotFoundException;
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

import java.util.*;

import static see.parser.antlr.ExceptionConverter.extractTokenPosition;

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
        final ContextCurriedFunction<Function<List<Object>, Object>> function = getFunction(functionName.getText());
        if (function == null){
            throw new FunctionNotFoundException(functionName.getText(), extractTokenPosition(functionName));
        }
        return function;
    }

    private ContextCurriedFunction<Function<List<Object>,Object>> getFunction(String functionName){
        return gc.getFunctions().get(functionName);
    }

    @Override
    public FunctionNode<Object, Object> createOperatorNode(Token functionName, Node<Object>... params) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunctionOrThrow(functionName);
        return new ImmutableFunctionNode<Object, Object>(func, Arrays.asList(params));
    }

    @Override
    public FunctionNode<Object, Object> createIfNode(Token ifToken, Node<Object> condition, Node<Object> thenNode, Node<Object> elseNode) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunctionOrThrow(ifToken);

        return new ImmutableFunctionNode<Object, Object>(func, Arrays.asList(condition, thenNode, elseNode));
    }

    @Override
    public FunctionNode<Object, Object> createSequence(List<Node<Object>> nodes) {
        ContextCurriedFunction<Function<List<Object>, Object>> func = getFunction("seq");

        if (nodes == null){
            return new ImmutableFunctionNode<Object, Object>(func);
        }else {
            return new ImmutableFunctionNode<Object, Object>(func, nodes);
        }

    }

    @Override
    public Node<Object> createUndefinedNode() {
        return new ImmutableConstNode<Object>(null);
    }
}
