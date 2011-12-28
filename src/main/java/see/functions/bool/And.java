package see.functions.bool;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Logical And function. Short-circuits.
 */
public class And implements VarArgFunction<Boolean, Boolean> {
    @Override
    public Boolean apply(@Nonnull List<Boolean> input) {
        for (Boolean value : input) {
            if (!value) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "and";
    }
}
