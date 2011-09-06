package see.parser.antlr.tree;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import see.tree.Visitor;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class DummyNode extends SeeTreeNode<Object> {


    public DummyNode(Token token) {
        super(token);
    }

    @Override
    public Object accept(Visitor visitor) {
        throw new UnsupportedOperationException("Dummy tree node, can't accept visitor");
    }

    @Override
    public Tree dupNode() {
        return new DummyNode(getToken());
    }

    @Override
    public String toString() {
        return new StringBuilder().append("dummy[").append(getText()).append("]").toString();
    }
}
