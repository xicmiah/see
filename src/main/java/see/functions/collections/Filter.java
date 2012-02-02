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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

public class Filter implements VarArgFunction<Object, Iterable<?>> {
    @Override
    public Iterable<?> apply(@Nonnull List<Object> args) {
        Preconditions.checkArgument(args.size() == 2, "Filter takes two arguments");

        Iterable<?> items = (Iterable<?>) args.get(0);
        final VarArgFunction<Object, Boolean> predicateFunction = (VarArgFunction<Object, Boolean>) args.get(1);

        Predicate<Object> predicate = new Predicate<Object>() {
            @Override
            public boolean apply(@Nullable Object input) {
                return predicateFunction.apply(singletonList(input));
            }
        };

        return filter(items, predicate);
    }

    /**
     * Choose appropriate filter based on runtime type of collection
     * @param items collection to transform
     * @param predicate predicate to match
     * @return lazy filtered collection
     */
    private Iterable<?> filter(Iterable<?> items, Predicate<Object> predicate) {
        if (items instanceof Set<?>) {
            return Sets.filter((Set<?>) items, predicate);
        } else if (items instanceof Collection<?>) {
            return Collections2.filter((Collection<?>) items, predicate);
        } else {
            return Iterables.filter(items, predicate);
        }
    }

    @Override
    public String toString() {
        return "filter";
    }
}
