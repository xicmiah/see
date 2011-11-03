package see.functions.compare;

import see.functions.Function;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Neq implements Function<List<Object>, Boolean> {
    private final Eq eq = new Eq();

    @Override
    public Boolean apply(List<Object> input) {
        checkArgument(input.size() == 2, "Neq takes only two arguments");

        boolean value = !eq.unwrappedApply(input.get(0), input.get(1));

        return value;
    }

    @Override
    public String toString() {
        return "!=";
    }
}
