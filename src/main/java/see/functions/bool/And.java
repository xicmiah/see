package see.functions.bool;

import see.functions.Function;

import java.util.List;

/**
 * Logical And function. Short-circuits.
 */
public class And implements Function<List<Boolean>, Boolean> {
    @Override
    public Boolean apply(List<Boolean> input) {
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
