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

package see.functions.collections;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

public class Transform implements VarArgFunction<Object, Iterable<?>> {
    @Override
    public Iterable<?> apply(@Nonnull final List<Object> args) {
        Preconditions.checkArgument(args.size() == 2, "Transform takes two arguments");
        
        Iterable<?> items = (Iterable<?>) args.get(0);
        final VarArgFunction<Object, ?> transformFunction = (VarArgFunction<Object, ?>) args.get(1);

        Function<Object, Object> transformation = new Function<Object, Object>() {
            @Override
            public Object apply(@Nullable Object input) {
                return transformFunction.apply(singletonList(input));
            }
        };

        return transform(items, transformation);
    }

    /**
     * Choose appropriate transformation based on runtime type of collection
     * @param items collection to transform
     * @param transformation item transformation
     * @return lazy transformed collection
     */
    private Iterable<?> transform(Iterable<?> items, Function<Object, Object> transformation) {
        if (items instanceof List<?>) {
            return Lists.transform((List<?>) items, transformation);
        } else if (items instanceof Collection<?>) {
            return Collections2.transform((Collection<?>) items, transformation);
        } else {
            return Iterables.transform(items, transformation);
        }
    }

    @Override
    public String toString() {
        return "map";
    }
}
