package see.functions.bool;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Logical Or function. Short-circuits.
 */
public class Or implements VarArgFunction<Boolean, Boolean> {
    @Override
    public Boolean apply(@Nonnull List<Boolean> input) {
        for (Boolean value : input) {
            if (value) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "or";
    }
}
