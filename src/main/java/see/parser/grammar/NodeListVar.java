package see.parser.grammar;

import com.google.common.collect.ImmutableList;
import org.parboiled.support.Var;
import see.tree.Node;

/**
 * Specialized version of Var for immutable lists of Nodes
 */
public class NodeListVar extends Var<ImmutableList<Node<Object>>> {
    public NodeListVar() {
        super(ImmutableList.<Node<Object>>of());
    }

    public boolean append(Node<Object> node) {
        return set(ImmutableList.<Node<Object>>builder().addAll(get()).add(node).build());
    }
}
