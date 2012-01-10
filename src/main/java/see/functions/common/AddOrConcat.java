package see.functions.common;

import see.functions.VarArgFunction;
import see.functions.arithmetic.Sum;
import see.functions.string.Concat;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

public class AddOrConcat implements VarArgFunction<Comparable, Comparable> {
    @Override
    public Comparable apply(@Nonnull List<Comparable> input) {
        Comparable firstElement = input.get(0);
        if (firstElement instanceof BigDecimal) {
            return new Sum().apply(input);
        }
        return new Concat().apply(input);
    }

    @Override
    public String toString() {
        return "addOrConcat";
    }
}
