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

import static com.google.common.collect.ImmutableSet.of;
import static junit.framework.Assert.assertEquals;

public class AbstractDependencyTest {

    ReactiveFactory reactiveFactory;

    @Before
    public void setUp() throws Exception {
        reactiveFactory = new ReactiveFactory();
    }

    @Test
    public void testInteraction() throws Exception {
        final VariableSignal<String> a = reactiveFactory.var("asd");
        Signal<Integer> b = reactiveFactory.bindWithState(of(a), new Supplier<Integer>() {
            @Override
            public Integer get() {
                return a.now().length();
            }
        });

        assertEquals(Integer.valueOf(3), b.now());
        a.update("omg");
        assertEquals(Integer.valueOf(3), b.now());
        a.update("zxcv");
        assertEquals(Integer.valueOf(4), b.now());
    }
}
