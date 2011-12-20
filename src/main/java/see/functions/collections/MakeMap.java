/*
 * Copyright 2011 Vasily Shiyan
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
import com.google.common.collect.ImmutableMap.Builder;
import see.functions.VarArgFunction;
import see.util.Reduce;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.builder;
import static com.google.common.collect.Lists.partition;
import static see.util.Reduce.fold;

public class MakeMap implements VarArgFunction<Object, Map<Object, Object>> {
    @Override
    public Map<Object, Object> apply(@Nonnull List<Object> input) {
        Preconditions.checkArgument(input.size() % 2 == 0, "MakeMap takes even number of arguments");

        Builder<Object, Object> builder = fold(
                builder(),
                partition(input, 2),
                new Reduce.FoldFunction<List<Object>, Builder<Object, Object>>() {
                    @Override
                    public Builder<Object, Object> apply(Builder<Object, Object> prev, List<Object> arg) {
                        return prev.put(arg.get(0), arg.get(1));
                    }
                });

        return builder.build();
    }

    @Override
    public String toString() {
        return "makeMap";
    }
}
