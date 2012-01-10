package see.functions.arithmetic;

import com.google.common.base.Preconditions;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class Min<T extends Comparable<T>> implements VarArgFunction<T, T> {
    @Override
    public T apply(@Nonnull List<T> input) {
        Preconditions.checkArgument(input.size() >= 0, "Min takes at least one argument");
        
        return Collections.min(input);
    }

    @Override
    public String toString() {
        return "Min";
    }
}
