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

package see.reactive;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import static com.google.common.base.Suppliers.compose;
import static com.google.common.collect.ImmutableSet.of;

/**
 * Utility methods for operations on signals.
 */
public abstract class Signals {
    private Signals() {}

    public static <A, B> Signal<B> transform(SignalFactory signalFactory, final Signal<A> signal, final Function<? super A, B> transformation) {
        return signalFactory.bind(of(signal), compose(transformation, signalSupplier(signal)));
    }

    public static <T> Supplier<T> signalSupplier(final Signal<T> signal) {
        return new Supplier<T>() {
            @Override
            public T get() {
                return signal.now();
            }
        };
    }

    public static <T> Signal<T> flatten(SignalFactory signalFactory, Signal<? extends Signal<T>> nested) {
        return transform(signalFactory, nested, Signals.<T>nowFunction());
    }

    public static <T> Function<Signal<T>, T> nowFunction() {
        return new Function<Signal<T>, T>() {
            @Override
            public T apply(Signal<T> signal) {
                return signal.now();
            }
        };
    }
}
