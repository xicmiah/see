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

package see.reactive.impl2;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import see.reactive.Signal;
import see.reactive.SignalFactory;
import see.reactive.Signals;
import see.reactive.VariableSignal;

import javax.annotation.Nonnull;
import java.util.Collection;

import static com.google.common.collect.ImmutableSet.of;

/**
 * Implementation of {@link SignalFactory}, which creates observer-based signals.
 */
public class AltSignalFactory implements SignalFactory {
    @Nonnull
    @Override
    public <T> VariableSignal<T> var(T initialValue) {
        return new Var<T>(initialValue);
    }

    @Nonnull
    @Override
    public <T> BoundSignal<T> bind(@Nonnull Collection<? extends Signal<?>> dependencies, @Nonnull Supplier<T> evaluation) {
        return new BoundSignal<T>(dependencies, evaluation);
    }

    @Override
    public <A, B> BoundSignal<B> map(Signal<A> signal, Function<? super A, B> transformation) {
        return bind(of(signal), Suppliers.compose(transformation, Signals.signalSupplier(signal)));
    }

    @Override
    public <A, B> Signal<B> flatMap(final Signal<A> signal, final Function<? super A, ? extends Signal<B>> transformation) {
        final DelegatingSignal<B> mirror = new DelegatingSignal<B>(transformation.apply(signal.now()));
        
        bind(of(signal), new OnSourceChange<A, B>(signal, mirror, transformation));

        return mirror;
    }

    private static class OnSourceChange<A, B> implements Supplier<Void> {
        private final Signal<A> source;
        private final DelegatingSignal<B> out;
        private final Function<? super A, ? extends Signal<B>> transformation;

        public OnSourceChange(Signal<A> source, DelegatingSignal<B> out, Function<? super A, ? extends Signal<B>> transformation) {
            this.out = out;
            this.transformation = transformation;
            this.source = source;
        }

        @Override
        public Void get() {
            out.setDelegate(transformation.apply(source.now()));
            return null;
        }
    }
}
