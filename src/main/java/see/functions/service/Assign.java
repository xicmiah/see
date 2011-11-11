package see.functions.service;

import com.google.common.base.Preconditions;
import see.functions.Settable;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Assignment.
 *
 * Sets new value via {@link Settable interface}
 * @param <T>
 */
public class Assign<T> implements VarArgFunction<Object, Object> {
    @Override
    public Object apply(@Nonnull List<Object> input) {
        Preconditions.checkArgument(input.size() == 2, "Assign takes two arguments");

        Settable<Object> target = (Settable<Object>) input.get(0);
        Object value = input.get(1);

        target.set(value);

        return value;
    }

    @Override
    public String toString() {
        return "assign";
    }
}
