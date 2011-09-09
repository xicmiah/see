package see.parser.antlr;

import org.antlr.runtime.Token;
import see.tree.ConstNode;
import see.tree.FunctionNode;
import see.tree.Node;
import see.tree.VarNode;

import java.util.List;

/**
 * @author pavlov
 * @since 09.09.11
 */
public interface SeeNodesFactory {
    ConstNode<Object> createStringNode(Token string);

    ConstNode<Object> createNumberNode(Token number);

    VarNode<Object> createVarNode(Token varName);

    FunctionNode<Object,Object> createFunctionNode(Token functionName, List<Node<Object>> params);

    FunctionNode<Object, Object> createOperatorNode(Token functionName, Node<Object>... params);

    FunctionNode<Object, Object> createIfNode(Token ifToken, Node<Object> condition,
                                              Node<Object> thenNode, Node<Object> elseNode);

    FunctionNode<Object, Object> createSequence(List<Node<Object>> nodes);

    Node<Object> createUndefinedNode();

}
