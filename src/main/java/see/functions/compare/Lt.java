package see.functions.compare;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Lt implements VarArgFunction<Comparable, Boolean> {
    @Override
    public Boolean apply(@Nonnull List<Comparable> input) {
        checkArgument(input.size() == 2, "Lt takes only two arguments");

        boolean value = input.get(0).compareTo(input.get(1)) < 0;

        return value;
    }

    @Override
    public String toString() {
        return "<";
    }
}
