package see.parser.antlr.tree;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import see.tree.ConstNode;
import see.tree.Visitor;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class ConstantTreeNode<T> extends SeeTreeNode<T> implements ConstNode<T> {

    private final T value;

    public ConstantTreeNode(Token token, T value) {
        super(token);
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public Tree dupNode() {
        return new ConstantTreeNode<T>(getToken(), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstantTreeNode that = (ConstantTreeNode) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
