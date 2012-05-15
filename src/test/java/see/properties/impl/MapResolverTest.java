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

package see.properties.impl;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.*;
import static see.parser.grammar.PropertyAccess.indexed;
import static see.parser.grammar.PropertyAccess.simple;

public class MapResolverTest {
    MapResolver resolver = new MapResolver();

    @Test
    public void testCanGet() throws Exception {
        assertTrue(resolver.canGet(Maps.newHashMap(), simple("c")));
        assertTrue(resolver.canGet(Maps.newHashMap(), indexed("c")));
        assertTrue(resolver.canGet(Maps.newHashMap(), indexed(new Object())));
    }

    @Test
    public void testGet() throws Exception {
        Map<String, Integer> map = of("c", 9);

        assertEquals(9, resolver.get(map, simple("c")));
        assertEquals(9, resolver.get(map, indexed("c")));
        assertNull(resolver.get(map, simple("d")));
    }

    @Test
    public void testCanSet() throws Exception {
        Map<String, Integer> map = Maps.newHashMap(of("c", 9));

        assertTrue(resolver.canSet(map, simple("c"), 42));
        assertTrue(resolver.canSet(map, indexed("c"), 42));
        assertTrue(resolver.canSet(map, indexed(new Object()), 42));
    }

    @Test
    public void testSet() throws Exception {
        Map<?, ?> map = Maps.newHashMap(of("c", 9));

        resolver.set(map, simple("c"), "bka");
        resolver.set(map, indexed(42), "answer");

        assertEquals("bka", map.get("c"));
        assertEquals("answer", map.get(42));
    }
}
