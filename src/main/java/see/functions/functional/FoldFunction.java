/*
 * Copyright 2012 Vasily Shiyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package see.functions.functional;

import com.google.common.base.Preconditions;
import see.functions.VarArgFunction;
import see.util.Reduce;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class FoldFunction implements VarArgFunction<Object, Object> {
    @Override
    public Object apply(@Nonnull final List<Object> args) {
        Preconditions.checkArgument(args.size() == 3, "Fold takes three arguments");

        Object initial = args.get(0);
        Iterable<?> items = (Iterable<?>) args.get(1);
        final VarArgFunction<Object, ?> foldFunction = (VarArgFunction<Object, ?>) args.get(2);

        Reduce.FoldFunction<Object, Object> folder = new Reduce.FoldFunction<Object, Object>() {
            @Override
            public Object apply(Object prev, Object arg) {
                return foldFunction.apply(Arrays.asList(prev, arg));
            }
        };

        return Reduce.fold(initial, items, folder);
    }

    @Override
    public String toString() {
        return "fold";
    }
}
