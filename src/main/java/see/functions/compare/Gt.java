package see.functions.compare;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Gt implements VarArgFunction<Comparable, Boolean> {
    @Override
    public Boolean apply(@Nonnull List<Comparable> input) {
        checkArgument(input.size() == 2, "Gt takes only two arguments");

        return input.get(0).compareTo(input.get(1)) > 0;
    }

    @Override
    public String toString() {
        return ">";
    }
}
