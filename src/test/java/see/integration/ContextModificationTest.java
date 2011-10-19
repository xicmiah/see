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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;
import see.See;
import see.exceptions.EvaluationException;
import see.tree.Node;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ContextModificationTest {
    See see = new See();

    /**
     * Test that evaluation can modify external context
     * @throws Exception
     */
    @Test
    public void testMutableContext() throws Exception {
        Map<String, Object> context = Maps.newHashMap();

        Node<Object> tree = see.parseExpressionList("a = \"value\";");
        see.evaluate(tree, context);

        assertEquals("value", context.get("a"));
    }

    /**
     * Test that evaluation fails on attempt to modify immutable context
     * @throws Exception
     */
    @Test(expected = EvaluationException.class)
    public void testAssignmentToImmutableContext() throws Exception {
        Node<?> tree = see.parseExpressionList("a = 5;");

        see.evaluate(tree, ImmutableMap.<String, Object>of());
    }
}
