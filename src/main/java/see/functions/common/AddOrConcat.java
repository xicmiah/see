package see.functions.common;

import see.functions.VarArgFunction;
import see.functions.arithmetic.Sum;
import see.functions.string.Concat;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

public class AddOrConcat implements VarArgFunction<Object, Object> {

    private final Sum sum = new Sum();
    private final Concat concat = new Concat();

    @Override
    public Object apply(@Nonnull List<Object> input) {
        Object firstElement = input.get(0);
        if (firstElement instanceof BigDecimal) {
            //noinspection unchecked
            List<BigDecimal> numArgs = (List) input;
            return sum.apply(numArgs);
        }
        return concat.apply(input);
    }

    @Override
    public String toString() {
        return "addOrConcat";
    }
}
