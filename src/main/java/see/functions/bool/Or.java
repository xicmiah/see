package see.functions.bool;

import see.functions.Function;

import java.util.List;

/**
 * Logical Or function. Short-circuits.
 */
public class Or implements Function<List<Boolean>, Boolean> {
    @Override
    public Boolean apply(List<Boolean> input) {
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
