package see.parser.antlr;

import org.antlr.runtime.ClassicToken;
import see.parser.antlr.tree.*;
import see.parser.config.GrammarConfiguration;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.VarNode;
import see.tree.immutable.ImmutableConstNode;
import see.tree.immutable.ImmutableFunctionNode;
import see.tree.immutable.ImmutableVarNode;

import java.util.Arrays;

/**
 * @author pavlov
 * @since 09.09.11
 */
public class ImmutableNodesBuilder {

    public static Node<Object> seq(GrammarConfiguration gc, Node<Object>... elements){
        return fun(gc, "seq", elements);
    }

    public static Node<Object> fun(GrammarConfiguration gc, String functionName, Node<Object>... args){
        return new ImmutableFunctionNode<Object, Object>(gc.getFunctions().get(functionName), Arrays.asList(args));
    }

    public static Node<Object> op(GrammarConfiguration gc, String opName, Node<Object>... args){
        return new ImmutableFunctionNode<Object, Object> (
                gc.getFunctions().get(opName),
                Arrays.asList(args)
        );
    }

    public static Node<Object> str(String value){
        return new ImmutableConstNode<Object>(value);
    }

    public static Node<Object> num(GrammarConfiguration gc, String value){
        return new ImmutableConstNode<Object>(gc.getNumberFactory().getNumber(value));
    }

    public static Node<Object> var(String value){
        return new ImmutableVarNode<Object> (value);
    }

}
