package see.functions;

import java.util.List;
import java.util.Map;

public class IsDefined implements ContextCurriedFunction<SingleArgFunction<String, Boolean>> {
    @Override
    public SingleArgFunction<String, Boolean> apply(final Map<String, Object> context) {
        return new SingleArgFunction<String, Boolean>() {
            @Override
            public Boolean apply(List<String> strings) {
                return context.get(strings.get(0)) != null;
            }
        };
    }
}
