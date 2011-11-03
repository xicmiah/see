package see.functions.bool;

import see.functions.Function;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Not implements Function<List<Boolean>, Boolean> {
    @Override
    public Boolean apply(List<Boolean> input) {
        checkArgument(input.size() == 1, "Not takes only one argument");
        checkNotNull(input.get(0));
        return !input.get(0);
    }

    @Override
    public String toString() {
        return "not";
    }
}