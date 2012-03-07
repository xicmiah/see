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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import see.functions.PartialFunction;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public abstract class FunctionUtils {
    private FunctionUtils() {}

    /**
     * Return partial function, which delegates to first applicable function from supplied
     * @param functions functions to aggregate
     * @param <A> common argument supertype
     * @param <R> common result supertype
     * @return created aggregating function
     */
    public static <A, R> PartialFunction<A, R> aggregate(final Iterable<? extends PartialFunction<? super A, ? extends R>> functions) {
        return new AggregatingFunction<A, R>(functions);
    }

    /**
     * Return partial function, which delegates to first applicable function from supplied
     * @param functions functions to aggregate
     * @param <A> common argument supertype
     * @param <R> common result supertype
     * @return created aggregating function
     */
    public static <A, R> PartialFunction<A, R> aggregate(PartialFunction<? super A, ? extends R>... functions) {
        return new AggregatingFunction<A, R>(asList(functions));
    }

    /**
     * Convert guava {@link Function} to equivalent var arg function.
     * @param f function to convert
     * @param <A> argument type
     * @param <R> result type
     * @return converted function
     */
    public static <A, R> VarArgFunction<A, R> toVarArg(final Function<? super List<A>, ? extends R> f) {
        if (f instanceof VarArgToGuava) {
            return ((VarArgToGuava) f).f; // Unwrap toFunction result
        }
        if (f instanceof VarArgFunction) {
            return (VarArgFunction<A, R>) f;
        }
        return new GuavaToVarArg<A, R>(f);
    }

    /**
     * Convert var arg function to equivalent guava function.
     * @param f function to convert
     * @param <A> argument type
     * @param <R> result type
     * @return converted function
     */
    public static <A, R> Function<List<A>, R> toFunction(final VarArgFunction<A, ? extends R> f) {
        if (f instanceof GuavaToVarArg) {
            return ((GuavaToVarArg) f).f; // Unwrap toVarArg result
        }
        if (f instanceof Function) {
            return (Function<List<A>, R>) f;
        }
        return new VarArgToGuava<A, R>(f);
    }

    /**
     * Converts single-argument {@link VarArgFunction} to guava {@link Function}
     * @param f function to convert
     * @param <A> argument type
     * @param <R> result type
     * @return corresponding guava function
     */
    public static <A, R> Function<A, R> singleArg(final VarArgFunction<A, R> f) {
        return new Function<A, R>() {
            @Override
            public R apply(@Nullable A input) {
                return f.apply(singletonList(input));
            }
        };
    }

    /**
     * Convert single-argument boolean {@link VarArgFunction} to guava {@link Predicate}
     * @param predicate function to convert
     * @param <A> argument type
     * @return corresponding guava predicate
     */
    public static <A> Predicate<A> singleArgPredicate(final VarArgFunction<A, Boolean> predicate) {
        return new Predicate<A>() {
            @Override
            public boolean apply(@Nullable A input) {
                return predicate.apply(singletonList(input));
            }
        };
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

    private static class AggregatingFunction<A, R> implements PartialFunction<A, R> {
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

    private static class GuavaToVarArg<A, R> implements VarArgFunction<A, R> {
        private final Function<? super List<A>, ? extends R> f;

        public GuavaToVarArg(Function<? super List<A>, ? extends R> f) {
            this.f = f;
        }

        @Override
        public R apply(@Nonnull List<A> input) {
            return f.apply(input);
        }
    }

    private static class VarArgToGuava<A, R> implements Function<List<A>, R> {
        private final VarArgFunction<A, ? extends R> f;

        public VarArgToGuava(VarArgFunction<A, ? extends R> f) {
            this.f = f;
        }

        @Override
        public R apply(List<A> input) {
            return f.apply(input);
        }
    }
}
