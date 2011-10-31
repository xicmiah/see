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
import see.functions.VarArgFunction;
import see.parser.config.ConfigBuilder;
import see.tree.Node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

public class PropertyTraversalTest {
    See see;
    
    TestBean bean;
    Map<String, ?> context;

    @Before
    public void setUp() throws Exception {
        VarArgFunction<Object, TestBean> beanFunc = new VarArgFunction<Object, TestBean>() {
            @Override
            public TestBean apply(List<Object> input) {
                return bean;
            }
        };
        see = new See(ConfigBuilder.defaultConfig().addPureFunction("bean", beanFunc).build());
        bean = new TestBean("second", new TestBean("first", null));
        context = of("a", bean);
    }

    /**
     * Test that 
     * @throws Exception
     */
    @Test
    public void testTraversal() throws Exception {
        assertEquals("second", see.eval("a.name", context));
        assertEquals(bean.getNext(), see.eval("a.next", context));
    }

    @Test
    public void testNestedTraversal() throws Exception {
        Node<?> tree = see.parseExpression("a.next.name");
        assertEquals("first", see.evaluate(tree, context));
    }

    @Test
    public void testAssignment() throws Exception {
        Node<?> tree = see.parseExpressionList("a.next.name = \"omg\";");
        
        see.evaluate(tree, context);
        
        assertEquals("omg", bean.getNext().getName());
    }

    @Test
    public void testChainedAssignment() throws Exception {
        Node<?> tree = see.parseExpressionList("b = a.name = a.next.name = \"omg\";");

        context = new HashMap<String, Object>(context); // Make context mutable
        
        see.evaluate(tree, context);

        assertEquals("omg", context.get("b"));
        assertEquals("omg", bean.getName());
        assertEquals("omg", bean.getNext().getName());
    }

    @Test
    public void testNonVariableSet() throws Exception {
        Node<?> tree = see.parseExpressionList("bean().name = \"omg\";");

        see.evaluate(tree, context);

        assertEquals("omg", bean.getName());
    }

    @Test
    public void testIndexed() throws Exception {
        assertEquals("second", see.eval("a['name']", context));
        assertEquals("first", see.eval("a['next']['name']", context));
        assertEquals("first", see.eval("bean()['next'].name", context));
    }

    @Test
    public void testVariableIndexes() throws Exception {
        Node<?> tree = see.parseExpressionList("p = 'next'; a[p].name;");
        assertEquals("first", see.evaluate(tree, newHashMap(context)));
    }

    @Test
    public void testNonVariableGet() throws Exception {
        assertEquals(bean.getName(), see.eval("bean().name"));
        assertEquals(bean.getNext().getName(), see.eval("bean().next.name"));
        assertEquals(BigDecimal.class, see.eval("5.class"));
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
