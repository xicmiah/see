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

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import see.See;
import see.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class ListTraversalTest {
    See see = new See();
    List<String> testList = ImmutableList.of("nine", "crno");

    @Test
    public void testListGet() throws Exception {
        Map<String, List<String>> context = of("a", testList);
        
        assertEquals("nine", see.eval("a[0]", context));
        assertEquals("crno", see.eval("a[1]", context));
    }

    @Test
    public void testListSet() throws Exception {
        List<String> mutableList = newArrayList(testList);
        Map<String, ?> context = of("a", mutableList);
        Node<?> tree = see.parseExpressionList("a[0] = '9'; a[1] = 'bka';");

        see.evaluate(tree, context);

        assertEquals(ImmutableList.of("9", "bka"), mutableList);
    }

    @Test
    public void testNonIndex() throws Exception {
        Map<String, ? extends List<Object>> context = of("a", newArrayList());
        assertEquals(ArrayList.class, see.eval("a.class", context));
    }
}
