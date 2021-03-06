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

package see.reactive;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ranges;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.ImmutableSet.of;
import static java.lang.Integer.valueOf;
import static junit.framework.Assert.assertEquals;

public abstract class SignalContractTest {

    SignalFactory signalFactory;

    @Before
    public void setUp() throws Exception {
        signalFactory = getSignalFactory();
    }

    protected abstract SignalFactory getSignalFactory();

    @Test
    public void testInteraction() throws Exception {
        final VariableSignal<String> a = signalFactory.var("asd");
        Signal<Integer> b = signalFactory.bind(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.now().length();
            }
        });

        assertEquals(valueOf(3), b.now());
        a.set("omg");
        assertEquals(valueOf(3), b.now());
        a.set("zxcv");
        assertEquals(valueOf(4), b.now());
    }

    @Test
    public void testMultipleDeps() throws Exception {
        final VariableSignal<Integer> a = signalFactory.var(1);
        final VariableSignal<Integer> b = signalFactory.var(2);

        Signal<Integer> sum = signalFactory.bind(of(a, b), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.now() + b.now();
            }
        });

        assertEquals(valueOf(3), sum.now());

        a.set(7);
        assertEquals(valueOf(9), sum.now());
        
        b.set(35);
        assertEquals(valueOf(42), sum.now());
    }

    @Test
    public void testStatefulSignal() throws Exception {
        final VariableSignal<String> a = signalFactory.var("crno");
        final AtomicInteger lengthCounter = new AtomicInteger(0);

        final Signal<Integer> length = signalFactory.bind(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                lengthCounter.incrementAndGet();
                return a.now().length();
            }
        });

        assertEquals(1, lengthCounter.get());

        final AtomicInteger plusCounter = new AtomicInteger(0);
        final Signal<Integer> plusOne = signalFactory.bind(of(length), new Supplier<Integer>() {
            @Override
            public Integer get() {
                plusCounter.incrementAndGet();
                return length.now() + 1;
            }
        });

        assertEquals(1, lengthCounter.get());
        assertEquals(1, plusCounter.get());

        a.set("bka");
        assertEquals(2, lengthCounter.get());
        assertEquals(2, plusCounter.get());

        assertEquals(valueOf(4), plusOne.now());
        assertEquals(2, lengthCounter.get());
        assertEquals(2, plusCounter.get());
    }

    /**
     * Test nested signals
     * @throws Exception
     */
    @Test
    public void testWeNeedToGoDeeper() throws Exception {
        VariableSignal<String> a = signalFactory.var("a");
        VariableSignal<String> b = signalFactory.var("b");

        VariableSignal<ImmutableSet<VariableSignal<String>>> deps = signalFactory.var(of(a));

        Signal<Signal<Collection<String>>> flatMapped = signalFlatMap(deps);

        assertEquals(of("a"), flatMapped.now().now());

        deps.set(of(a, b));
        assertEquals(of("a", "b"), flatMapped.now().now());
        
        a.set("crn");
        assertEquals(of("crn", "b"), flatMapped.now().now());

        b.set("bka");
        assertEquals(of("crn", "bka"), flatMapped.now().now());
    }

    @Test
    public void testNestedSetters() throws Exception {
        VariableSignal<String> a = signalFactory.var("a");
        VariableSignal<String> b = signalFactory.var("b");

        VariableSignal<ImmutableSet<VariableSignal<String>>> deps = signalFactory.var(of(a));

        final Signal<Collection<String>> flat = signalFactory.flatMap(deps, new Function<Collection<? extends Signal<String>>, Signal<Collection<String>>>() {
            @Override
            public Signal<Collection<String>> apply(Collection<? extends Signal<String>> signals) {
                return mergeSignals(signals);
            }
        });

        final AtomicReference<Collection<String>> sink = getSink(flat);

        assertEquals(of("a"), sink.get());

        deps.set(of(a, b));
        assertEquals(of("a", "b"), sink.get());
        
        b.set("bka");
        assertEquals(of("a", "bka"), sink.get());

        deps.set(of(b));
        assertEquals(of("bka"), sink.get());
    }

    @Test
    public void testFlatMap() throws Exception {
        VariableSignal<String> a = signalFactory.var("a");
        VariableSignal<String> b = signalFactory.var("b");
        VariableSignal<VariableSignal<String>> ref = signalFactory.var(a);

        Signal<String> value = signalFactory.flatMap(ref, Functions.<Signal<String>>identity());
        assertEquals("a", value.now());
        ref.set(b);
        assertEquals("b", value.now());
        b.set("bka");
        assertEquals("bka", value.now());
    }

    @Test
    public void testFlatMapMemory() throws Exception {
        VariableSignal<String> a = signalFactory.var("a");
        VariableSignal<String> b = signalFactory.var("b");
        VariableSignal<ImmutableSet<VariableSignal<String>>> deps = signalFactory.var(of(a));

        Signal<Collection<String>> flat = signalFactory.flatMap(deps, new SignalMerge());

        for (Integer i : Ranges.closed(1, 5).asSet(DiscreteDomains.integers())) {
            deps.set(of(a, b));

            a.set("a1");
            b.set("b1");

            deps.set(of(a));

            a.set("a");
            b.set("b");
        }

        System.gc();

        assertEquals(of("a"), flat.now());
    }

    private <T> AtomicReference<T> getSink(final Signal<? extends T> signal) {
        final AtomicReference<T> sink = new AtomicReference<T>();

        signalFactory.bind(of(signal), new Supplier<Void>() {
            @Override
            public Void get() {
                sink.set(signal.now());
                return null;
            }
        });
        return sink;
    }

    private <T> Signal<Signal<Collection<T>>> signalFlatMap(final Signal<? extends Collection<? extends Signal<T>>> nested) {
        return signalFactory.map(nested, new Function<Collection<? extends Signal<T>>, Signal<Collection<T>>>() {
            @Override
            public Signal<Collection<T>> apply(Collection<? extends Signal<T>> signals) {
                return mergeSignals(signals);
            }
        });
    }

    private <T> Signal<Collection<T>> mergeSignals(final Collection<? extends Signal<T>> signals) {
        return signalFactory.bind(signals, new Supplier<Collection<T>>() {
            @Override
            public Collection<T> get() {
                return copyOf(transform(signals, Signals.<T>nowFunction()));
            }
        });
    }

    private class SignalMerge implements Function<Collection<? extends Signal<String>>, Signal<Collection<String>>> {
        @Override
        public Signal<Collection<String>> apply(Collection<? extends Signal<String>> signals) {
            return mergeSignals(signals);
        }
    }
}
