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

import org.junit.Test;
import see.See;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyTraversalTest {
    See see = new See();

    /**
     * Test that 
     * @throws Exception
     */
    @Test
    public void testTraversal() throws Exception {
        TestBean bean = new TestBean("asd", null);
        Map<String, Object> context = of("a", (Object) bean);

        assertEquals("asd", see.eval("a.name", context));
        assertNull(see.eval("a.next", context));
    }

    @Test
    public void testNestedTraversal() throws Exception {
        TestBean first = new TestBean("first", null);
        TestBean second = new TestBean("second", first);

        Node<Object> tree = see.parseExpression("a.next.name");
        assertEquals("first", see.evaluate(tree, of("a", (Object) second)));
    }

    public static class TestBean {
        private String name;
        private TestBean next;

        public TestBean() {
        }

        public TestBean(String name, TestBean next) {
            this.name = name;
            this.next = next;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TestBean getNext() {
            return next;
        }

        public void setNext(TestBean next) {
            this.next = next;
        }
    }
}
