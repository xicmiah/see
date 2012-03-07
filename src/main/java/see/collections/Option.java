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

package see.collections;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Iterators.emptyIterator;
import static com.google.common.collect.Iterators.singletonIterator;
import static see.util.FunctionUtils.singleArg;
import static see.util.FunctionUtils.singleArgPredicate;

public abstract class Option<T> extends AbstractSet<T> implements Set<T> {
    private Option() {}

    private static final None<Object> noneInstance = new None<Object>();

    public static <T> Option<T> fromNullable(@Nullable T value) {
        if (value == null) {
            return absent();
        } else {
            return of(value);
        }
    }

    public static <T> Option<T> of(@Nonnull T value) {
        return new Some<T>(value);
    }

    public static <T> Option<T> absent() {
        return (Option<T>) noneInstance;
    }

    public abstract <R> Option<R> map(Function<? super T, ? extends R> function);

    public abstract Option<T> filter(Predicate<? super T> predicate);

    public abstract <R> Option<R> flatMap(Function<? super T, ? extends Option<R>> function);

    public <R> Option<R> map(VarArgFunction<? super T, ? extends R> function) {
        return map(singleArg(function));
    }

    public Option<T> filter(VarArgFunction<? super T, Boolean> predicate) {
        return filter(singleArgPredicate(predicate));
    }

    public <R> Option<R> flatMap(VarArgFunction<? super T, ? extends Option<R>> function) {
        return flatMap(singleArg(function));
    }

    @Nullable
    public abstract T get();

    public T getOrElse(T defaultValue) {
        T value = get();
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    private static class Some<T> extends Option<T> {
        @Nonnull
        private final T value;

        private Some(@Nonnull T value) {
            this.value = value;
        }

        @Override
        public Iterator<T> iterator() {
            return singletonIterator(value);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public <R> Option<R> map(Function<? super T, ? extends R> function) {
            return fromNullable(function.apply(value));
        }

        @Override
        public Option<T> filter(Predicate<? super T> predicate) {
            if (predicate.apply(value)) {
                return this;
            } else {
                return absent();
            }
        }

        @Override
        public <R> Option<R> flatMap(Function<? super T, ? extends Option<R>> function) {
            return function.apply(value);
        }

        @Override
        public T get() {
            return value;
        }
    }

    private static class None<T> extends Option<T> {
        @Override
        public Iterator<T> iterator() {
            return emptyIterator();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public <R> Option<R> map(Function<? super T, ? extends R> function) {
            return absent();
        }

        @Override
        public Option<T> filter(Predicate<? super T> predicate) {
            return absent();
        }

        @Override
        public <R> Option<R> flatMap(Function<? super T, ? extends Option<R>> function) {
            return absent();
        }

        @Override
        public T get() {
            return null;
        }
    }
}
