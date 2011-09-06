package see.functions.arithmetic;

import com.google.common.base.Preconditions;
import see.functions.VarArgFunction;

import java.util.Collections;
import java.util.List;

public class Max<T extends Comparable<T>> implements VarArgFunction<T, T> {

    @Override
    public T apply(List<T> input) {
        Preconditions.checkArgument(input.size() >= 0, "Max takes at least one argument");

        return Collections.max(input);
    }

    @Override
    public String toString() {
        return "Max";
    }
}
