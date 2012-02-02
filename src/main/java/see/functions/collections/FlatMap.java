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
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static java.util.Collections.singletonList;
import static see.util.Reduce.flatMap;

public class FlatMap implements VarArgFunction<Object, Iterable<?>> {
    @Override
    public Iterable<?> apply(@Nonnull List<Object> args) {

        Iterable<?> items = (Iterable<?>) args.get(0);
        final VarArgFunction<Object, Iterable<Object>> transformation = (VarArgFunction<Object, Iterable<Object>>) args.get(1);

        Function<Object, Iterable<Object>> flatMapFunc = new Function<Object, Iterable<Object>>() {
            @Override
            public Iterable<Object> apply(@Nullable Object input) {
                return transformation.apply(singletonList(input));
            }
        };
        return flatMap(items, flatMapFunc);
    }

    @Override
    public String toString() {
        return "flatMap";
    }
}
