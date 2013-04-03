package see.functions.service;


import com.google.common.base.Preconditions;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

public class IsDefined implements VarArgFunction<Object, Boolean> {

    @Override
    public Boolean apply(@Nonnull List<Object> args) {
        Preconditions.checkArgument(args.size() == 1, "isDefined takes one argument");

        try {
            Object value = args.get(0);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "isDefined";
    }
}
