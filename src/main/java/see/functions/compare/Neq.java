package see.functions.compare;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Neq implements VarArgFunction<Object, Boolean> {
    private final Eq eq = new Eq();

    @Override
    public Boolean apply(@Nonnull List<Object> input) {
        checkArgument(input.size() == 2, "Neq takes only two arguments");

        return !eq.apply(input);
    }

    @Override
    public String toString() {
        return "!=";
    }
}
