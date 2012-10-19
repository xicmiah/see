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

package see.integration;

import org.junit.Test;
import see.ReactiveSee;
import see.reactive.SignalFactory;
import see.reactive.impl.OrderedSignalFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test that old and new signal expressions are parsed into same tree
 */
public class AltBindingTest {
    SignalFactory signalFactory = new OrderedSignalFactory();
    ReactiveSee see = new ReactiveSee(signalFactory);

    @Test
    public void testBindEquality() throws Exception {
        assertEquals(see.parseExpression("a.b << c() + 9"), see.parseExpression("a.b << c() + 9"));
    }

    @Test
    public void testSignalEquality() throws Exception {
        assertEquals(see.parseExpression("a = signal(b() + 42)"), see.parseExpression("a <<= b() + 42"));
    }
}
