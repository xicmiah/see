package see.parser.grammar;

import com.google.common.collect.ImmutableList;
import org.parboiled.support.Var;

import java.util.List;

/**
 * Specialized version of Var for immutable lists
 */
public class ListVar<T> extends Var<List<T>> {
    public ListVar() {
        super(ImmutableList.<T>of());
    }

    /**
     * Append value to contained list.
     * @param value value to append
     * @return true
     */
    public boolean append(T value) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        builder.addAll(get());
        builder.add(value);
        return set(builder.build());
    }
}
