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

package see.functions.reactive;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import see.evaluation.Context;
import see.evaluation.ValueProcessor;
import see.evaluation.visitors.EagerVisitor;
import see.evaluation.visitors.LazyVisitor;
import see.functions.ContextCurriedFunction;
import see.functions.VarArgFunction;
import see.properties.ChainResolver;
import see.reactive.Signal;
import see.reactive.impl.ReactiveFactory;
import see.tree.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import static see.evaluation.processors.AggregatingProcessor.concat;

/**
 * Signal creation. Expects second argument to be a tree.
 * Creates a signal bound to signals present in tree.
 */
public class MakeSignal implements ContextCurriedFunction<VarArgFunction<Object, Signal<?>>> {
    @Override
    public VarArgFunction<Object, Signal<?>> apply(@Nonnull final Context context) {
        return new VarArgFunction<Object, Signal<?>>() {
            @Override
            public Signal<?> apply(@Nonnull List<Object> input) {
                Preconditions.checkArgument(input.size() == 2, "MakeSignal takes two arguments");

                final Node<Object> tree = (Node<Object>) input.get(1);

                ChainResolver resolver = context.getService(ChainResolver.class);
                ValueProcessor processor = context.getService(ValueProcessor.class);

                SignalCapture signalCapture = new SignalCapture();
                EagerVisitor eagerVisitor = new EagerVisitor(context, concat(signalCapture, processor), resolver);
                tree.accept(eagerVisitor);

                final LazyVisitor lazyVisitor = new LazyVisitor(context, concat(new SignalExpand(), processor), resolver);
                return context.getService(ReactiveFactory.class).bind(signalCapture.dependencies, new Supplier<Object>() {
                    @Override
                    public Object get() {
                        return tree.accept(lazyVisitor);
                    }
                });

            }
        };
    }

    @Override
    public String toString() {
        return "signal";
    }

    /**
     * Processor, which expands signals to their current value.
     */
    private static final class SignalExpand implements ValueProcessor {
        @Override
        public Object apply(@Nullable Object input) {
            if (input instanceof Signal<?>) {
                Signal<?> signal = (Signal<?>) input;
                return signal.getNow();
            }
            return input;
        }
    }

    /**
     * Processor, which expands signals to their current value, capturing dependencies.
     */
    private static final class SignalCapture implements ValueProcessor {
        private final Collection<Signal<?>> dependencies = Sets.newHashSet();

        @Override
        public Object apply(@Nullable Object input) {
            if (input instanceof Signal<?>) {
                Signal<?> signal = (Signal<?>) input;
                dependencies.add(signal);
                return signal.getNow();
            }
            return input;
        }

    }
}
