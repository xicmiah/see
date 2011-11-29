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

package see.functions.service;

import com.google.common.base.Preconditions;
import see.functions.ContextCurriedFunction;
import see.functions.Function;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Function application. Evaluates supplied function with supplied arguments.
 * First argument is {@link Function} instance, rest are it's arguments.
 * Returns evaluation result.
 */
public class Apply implements ContextCurriedFunction<Function<List<Object>, Object>> {
    @Override
    public Function<List<Object>, Object> apply(final Map<String, ?> context) {
        return new Function<List<Object>, Object>() {
            @Override
            public Object apply(@Nonnull List<Object> input) {
                Preconditions.checkArgument(input.size() >= 1, "Apply takes one or more arguments");

                ContextCurriedFunction<Function<List<Object>, Object>> target = (ContextCurriedFunction<Function<List<Object>, Object>>) input.get(0);

                return target.apply(context).apply(input.subList(1, input.size()));
            }
        };
    }

    @Override
    public String toString() {
        return "apply";
    }
}
