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

package see.evaluation.conversions;

import see.evaluation.ToFunction;
import see.functions.Function;
import see.functions.PartialFunction;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class FunctionConversions {
    private FunctionConversions() {}

    public static ToFunction lift(final PartialFunction<Object, Function<List<Object>, ?>> f) {
        if (f instanceof ToFunction) {
            return (ToFunction) f;
        }

        return new ToFunction() {
            @Nonnull
            @Override
            public Function<List<Object>, ?> apply(@Nonnull Object input) {
                return f.apply(input);
            }

            @Override
            public boolean isDefinedAt(Object input) {
                return f.isDefinedAt(input);
            }
        };
    }
}
