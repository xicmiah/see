package see.functions.string;

import see.functions.Function;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

public class Concat implements Function<List<Comparable>, Comparable> {
    @Override
    public Comparable apply(@Nonnull List<Comparable> input) {
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
