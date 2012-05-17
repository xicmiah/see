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

package see.reactive.impl;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import see.reactive.Signal;
import see.reactive.SignalFactory;
import see.reactive.VariableSignal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Suppliers.compose;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Iterables.all;
import static see.reactive.Signals.signalSupplier;

public class OrderedSignalFactory implements SignalFactory {
    @Nonnull
    @Override
    public <T> VariableSignal<T> var(@Nullable T initialValue) {
        return new Var<T>(initialValue);
    }

    @Nonnull
    @Override
    public <T> Signal<T> bind(@Nonnull Collection<? extends Signal<?>> dependencies, @Nonnull Supplier<T> evaluation) {
        checkArgument(all(dependencies, instanceOf(AbstractOrderedSignal.class)));
        @SuppressWarnings("unchecked") // safe to cast, already checked
        Collection<AbstractOrderedSignal<?>> casted = (Collection<AbstractOrderedSignal<?>>) dependencies;
        return new BoundSignal<T>(casted, evaluation);
    }

    @Override
    public <A, B> Signal<B> map(final Signal<A> signal, final Function<? super A, B> transformation) {
        return bind(of(signal), compose(transformation, signalSupplier(signal)));
    }

    @Override
    public <A, B> Signal<B> flatMap(final Signal<A> signal,
                                    final Function<? super A, ? extends Signal<B>> transformation) {
        final DelegatingSignal<B> mirror = DelegatingSignal.create(transformation.apply(signal.now()));

        bind(of(signal), new Supplier<Void>() {
            @Override
            public Void get() {
                mirror.setDelegate(transformation.apply(signal.now()));
                return null;
            }
        });

        return mirror;
    }
}
