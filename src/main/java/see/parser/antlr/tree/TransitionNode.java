package see.parser.antlr.tree;

import com.google.common.base.Objects;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import see.tree.Visitor;

/**
 * @author pavlov
 * @since 08.09.11
 */
public class TransitionNode<T> extends SeeTreeNode<T>{

    public TransitionNode(Token token) {
        super(token);
    }

    @Override
    public T accept(Visitor visitor) {
        return getValuableChild().accept(visitor);
    }

    @Override
    public Tree dupNode() {
        return new TransitionNode(getToken());
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("transition[")
                .append(getValuableChild().toString())
                .append("]")
                .toString();
    }

    @SuppressWarnings({"unchecked"})
    private SeeTreeNode<T> getValuableChild(){
        return (SeeTreeNode<T>) getChild(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !TransitionNode.class.equals(o.getClass())) return false;

        TransitionNode that = (TransitionNode) o;

        return Objects.equal(getValuableChild(), that.getValuableChild());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValuableChild());
    }
}
