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
import com.google.common.collect.Sets;
import org.junit.Test;
import see.reactive.impl3.OrderedSignalFactory;

import java.util.Collection;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;

public class OrderedSignalContractTest extends SignalContractTest {
    @Override
    protected SignalFactory getSignalFactory() {
        return new OrderedSignalFactory();
    }

    /**
     * Test absence of glitches.
     * Signal dependencies: zero->(x->source, -x->source).
     * Depth-first change propagation will produce a glitch here.
     */
    @Test
    public void testGlitches() throws Exception {
        final VariableSignal<Integer> source = signalFactory.var(5);

        final Signal<Integer> x = signalFactory.map(source, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input;
            }
        });
        final Signal<Integer> minusX = signalFactory.map(source, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return -input;
            }
        });

        // -x + x: should always be zero
        Signal<Integer> zero = signalFactory.bind(of(x, minusX), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return x.now() + minusX.now();
            }
        });

        final Collection<Integer> observedValues = Sets.newTreeSet();
        Signal<Void> sink = signalFactory.map(zero, new Function<Integer, Void>() {
            @Override
            public Void apply(Integer input) {
                observedValues.add(input);
                return null;
            }
        });

        source.set(1);
        assertEquals(of(0), copyOf(observedValues));
    }
}
