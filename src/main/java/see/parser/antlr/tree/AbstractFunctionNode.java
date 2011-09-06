package see.parser.antlr.tree;

import com.google.common.base.Objects;
import org.antlr.runtime.Token;
import see.tree.FunctionNode;
import see.tree.Visitor;

/**
 * @author pavlov
 * @since 06.09.11
 */
public abstract class AbstractFunctionNode<Arg, Result>
        extends SeeTreeNode<Result> implements FunctionNode<Arg,Result> {

    protected AbstractFunctionNode(Token token) {
        super(token);
    }

    @Override
    public Result accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AbstractFunctionNode)) return false;

        AbstractFunctionNode that = (AbstractFunctionNode) o;

        return Objects.equal(getFunction(), that.getFunction()) && Objects.equal(getArguments(), that.getArguments());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFunction(), getArguments());
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(getFunction())
                .append(getArguments())
                .toString();
    }
}
