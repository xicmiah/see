package see.parser.antlr.tree;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import see.tree.VarNode;
import see.tree.Visitor;

/**
 * @author pavlov
 * @since 06.09.11
 */
public class VarTreeNode<T> extends SeeTreeNode<T> implements VarNode<T>{

    public VarTreeNode(Token token) {
        super(token);
    }

    @Override
    public T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getName() {
        return getToken().getText();
    }

    @Override
    public Tree dupNode() {
        return new VarTreeNode(getToken());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VarTreeNode that = (VarTreeNode) o;

        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("Var(").append(getName()).append(")").toString();
    }
}
