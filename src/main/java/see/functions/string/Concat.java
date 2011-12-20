package see.functions.string;

import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

public class Concat implements VarArgFunction<Comparable, String> {
    @Override
    public String apply(@Nonnull List<Comparable> input) {
        StringBuilder sb = new StringBuilder();
        for (Comparable str : input) {
            String v = (String) str;
            sb.append(v);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "append";
    }
}
