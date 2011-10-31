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

package see.integration;

import org.junit.Before;
import org.junit.Test;
import see.See;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MapTraversalTest {
    See see = new See();
    Map<String, ?> testMap;
    Map<String, ? extends Map<String, ?>> context;

    @Before
    public void setUp() throws Exception {
        testMap = of("item", "content");
        context = of("a", testMap);
    }

    @Test
    public void testMapRead() throws Exception {
        assertEquals("content", see.eval("a['item']", context));
        assertEquals("content", see.eval("a.item", context));
    }

    @Test
    public void testMapWrite() throws Exception {
        context = of("a", newHashMap(testMap)); // make testMap mutable
        Node<?> tree = see.parseExpressionList("a.item = 'omg'; a.crn = '09';");
        see.evaluate(tree, context);

        assertEquals(of("item", "omg", "crn", "09"), context.get("a"));
    }

    @Test
    public void testPriority() throws Exception {
        assertNull(see.eval("a.class", context)); // should be testMap.get("class"), NOT testMap.getClass()
    }
}
