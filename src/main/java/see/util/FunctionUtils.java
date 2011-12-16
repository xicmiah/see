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

package see.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import see.functions.PartialFunction;

import javax.annotation.Nonnull;

public abstract class FunctionUtils {
    private FunctionUtils() {}

    public <A, R> PartialFunction<A, R> aggregate(final Iterable<? extends PartialFunction<? super A, ? extends R>> functions) {
        return new AggregatingFunction<A, R>(functions);
    }

    private static class IsDefinedPredicate<A, R> implements Predicate<PartialFunction<? super A, ? extends R>> {
        private final A input;

        public IsDefinedPredicate(A input) {
            this.input = input;
        }

        @Override
        public boolean apply(PartialFunction<? super A, ? extends R> function) {
            return function.isDefinedAt(input);
        }
    }

    private class AggregatingFunction<A, R> implements PartialFunction<A, R> {
        private final Iterable<? extends PartialFunction<? super A, ? extends R>> functions;

        public AggregatingFunction(Iterable<? extends PartialFunction<? super A, ? extends R>> functions) {
            this.functions = functions;
        }

        @Override
        public R apply(@Nonnull A input) {
            return Iterables.find(functions, new IsDefinedPredicate<A, R>(input)).apply(input);
        }

        @Override
        public boolean isDefinedAt(final A input) {
            return Iterables.any(functions, new IsDefinedPredicate<A, R>(input));
        }
    }
}
