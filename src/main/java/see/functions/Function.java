package see.functions;

import javax.annotation.Nonnull;

public interface Function<Arg, Result> {
    Result apply(@Nonnull Arg input);
}
