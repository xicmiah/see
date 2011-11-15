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
import see.evaluator.ContextualVisitor;
import see.evaluator.NumberLifter;
import see.evaluator.ValueProcessor;
import see.parser.numbers.NumberFactory;
import see.reactive.Dependency;
import see.reactive.Signal;
import see.reactive.impl.ReactiveFactory;
import see.tree.Node;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;

/**
 * Signal creation. Expects second argument to be a tree.
 * Creates a
 */
public class MakeSignal extends ReactiveFunction<Object, Signal<?>> {

    private final Supplier<NumberFactory> factorySupplier;

    public MakeSignal(Supplier<NumberFactory> factorySupplier) {
        this.factorySupplier = factorySupplier;
    }

    @Override
    public Signal<?> apply(ReactiveFactory factory, final List<Object> input, final Map<String, ?> context) {
        Preconditions.checkArgument(input.size() == 2, "MakeSignal takes two arguments");

        final Node<Object> tree = (Node<Object>) input.get(1);

        final SignalCapture signalCapture = new SignalCapture();
        final SignalExpand signalExpand = new SignalExpand();
        final NumberLifter numberLifter = new NumberLifter(factorySupplier);

        tree.accept(new ContextualVisitor(context, of(signalCapture, numberLifter)));

        return factory.bind(signalCapture.dependencies, new Supplier<Object>() {
            @Override
            public Object get() {
                return tree.accept(new ContextualVisitor(context, of(signalExpand, numberLifter)));
            }
        });
    }

    @Override
    public String toString() {
        return "signal";
    }

    private static final class SignalCapture implements ValueProcessor {
        private final Collection<Dependency> dependencies = Sets.newHashSet();

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
}
