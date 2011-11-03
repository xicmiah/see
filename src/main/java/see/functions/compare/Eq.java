package see.functions.compare;

import com.google.common.base.Objects;
import see.functions.VarArgFunction;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Equality. Returns if it's arguments are equal.
 * This implementation
 */
public class Eq implements VarArgFunction<Object, Boolean> {

    @Override
    public Boolean apply(List<Object> input) {
        checkArgument(input.size() == 2, "Eq takes two arguments");

        Object arg1 = input.get(0);
        Object arg2 = input.get(1);

        return unwrappedApply(arg1, arg2);
    }

    public boolean unwrappedApply(Object arg1, Object arg2) {
        if (canUseCompareTo(arg1, arg2)) {
            return ((Comparable) arg1).compareTo((Comparable) arg2) == 0;
        } else {
            return Objects.equal(arg1, arg2);
        }
    }

    private boolean canUseCompareTo(Object arg1, Object arg2) {
        return arg1 instanceof Comparable && arg2 instanceof Comparable && arg1.getClass() == arg2.getClass();
    }

    @Override
    public String toString() {
        return "==";
    }
}
