package see.functions.string;

import com.google.common.base.Joiner;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

public class Concat implements VarArgFunction<Object, String> {

    private final Joiner joiner = Joiner.on("").useForNull("null");

    @Override
    public String apply(@Nonnull List<Object> input) {
        return joiner.join(input);
    }

    @Override
    public String toString() {
        return "append";
    }
}
