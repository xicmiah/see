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

import com.google.common.base.Supplier;
import org.junit.Before;
import org.junit.Test;
import see.reactive.impl.ReactiveFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.ImmutableSet.of;
import static java.lang.Integer.valueOf;
import static junit.framework.Assert.assertEquals;

public class ReactiveTest {

    ReactiveFactory reactiveFactory;

    @Before
    public void setUp() throws Exception {
        reactiveFactory = new ReactiveFactory();
    }

    @Test
    public void testInteraction() throws Exception {
        final VariableSignal<String> a = reactiveFactory.var("asd");
        Signal<Integer> b = reactiveFactory.bind(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.getNow().length();
            }
        });

        assertEquals(valueOf(3), b.getNow());
        a.update("omg");
        assertEquals(valueOf(3), b.getNow());
        a.update("zxcv");
        assertEquals(valueOf(4), b.getNow());
    }

    @Test
    public void testMultipleDeps() throws Exception {
        final VariableSignal<Integer> a = reactiveFactory.var(1);
        final VariableSignal<Integer> b = reactiveFactory.var(2);

        Signal<Integer> sum = reactiveFactory.bind(of(a, b), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.getNow() + b.getNow();
            }
        });

        assertEquals(valueOf(3), sum.getNow());

        a.update(7);
        assertEquals(valueOf(9), sum.getNow());
        
        b.update(35);
        assertEquals(valueOf(42), sum.getNow());
    }

    @Test
    public void testStatelessSignal() throws Exception {
        final VariableSignal<String> a = reactiveFactory.var("crno");
        final AtomicInteger counter = new AtomicInteger(0);

        Signal<Integer> b = reactiveFactory.bindLazy(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                counter.incrementAndGet();
                return a.getNow().length();
            }
        });

        assertEquals(0, counter.get());

        assertEquals(valueOf(4), b.getNow());
        assertEquals(1, counter.get());

        assertEquals(valueOf(4), b.getNow());
        assertEquals(2, counter.get());
        
        a.update("bka");
        assertEquals(2, counter.get());
        assertEquals(valueOf(3), b.getNow());
        assertEquals(3, counter.get());

    }

    @Test
    public void testStatefulSignal() throws Exception {
        final VariableSignal<String> a = reactiveFactory.var("crno");
        final AtomicInteger lengthCounter = new AtomicInteger(0);

        final Signal<Integer> length = reactiveFactory.bind(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                lengthCounter.incrementAndGet();
                return a.getNow().length();
            }
        });

        assertEquals(1, lengthCounter.get());

        final AtomicInteger plusCounter = new AtomicInteger(0);
        final Signal<Integer> plusOne = reactiveFactory.bind(of(length), new Supplier<Integer>() {
            @Override
            public Integer get() {
                plusCounter.incrementAndGet();
                return length.getNow() + 1;
            }
        });

        assertEquals(1, lengthCounter.get());
        assertEquals(1, plusCounter.get());

        a.update("bka");
        assertEquals(2, lengthCounter.get());
        assertEquals(2, plusCounter.get());

        assertEquals(valueOf(4), plusOne.getNow());
        assertEquals(2, lengthCounter.get());
        assertEquals(2, plusCounter.get());
    }
}
