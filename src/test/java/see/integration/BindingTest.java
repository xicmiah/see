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
import org.junit.Before;
import org.junit.Test;
import see.ReactiveSee;
import see.reactive.Signal;
import see.reactive.SignalFactory;
import see.reactive.VariableSignal;
import see.reactive.impl.OrderedSignalFactory;
import see.tree.Node;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.*;

public class BindingTest {
    SignalFactory signalFactory = new OrderedSignalFactory();
    ReactiveSee see;

    protected ReactiveSee createReactiveSee() {
        return new ReactiveSee(signalFactory);
    }


    @Before
    public void setUp() throws Exception {
        see = createReactiveSee();
    }

    @Test
    public void testBindings() throws Exception {
        VariableSignal<String> var = signalFactory.var("crno");
        PropertyTraversalTest.TestBean bean = new PropertyTraversalTest.TestBean("omg", null);

        Map<String, Object> context = of("a", bean, "v", var);
        
        see.eval("a.name <- v()", context);
        assertEquals("crno", bean.getName());

        var.set("bka");
        assertEquals("bka", bean.getName());
    }

    @Test
    public void testMultipleBindings() throws Exception {
        VariableSignal<Integer> a = signalFactory.var(1);
        VariableSignal<Integer> b = signalFactory.var(2);
        TestBean bean = new TestBean();

        Map<String, Object> context = of("a", a, "b", b, "bean", bean);

        see.eval("bean.value <- a() + b()", context);
        assertEquals("3.0", bean.getValue().toString());
        
        a.set(7);
        assertEquals("9.0", bean.getValue().toString());

        b.set(35);
        assertEquals("42.0", bean.getValue().toString());
    }

    @Test
    public void testBindingContext() throws Exception {
        VariableSignal<Integer> a = signalFactory.var(7);
        TestBean bean = new TestBean();

        Map<String, Object> context = Maps.newHashMap(of("a", a, "b", 2, "bean", bean));

        see.eval("bean.value <- a() + b", context);
        assertEquals("9.0", bean.getValue().toString());

        context.put("b", 42);
        assertEquals("9.0", bean.getValue().toString());

        a.set(40);
        assertEquals("42.0", bean.getValue().toString()); // Signal is bound to old b == 42
    }

    @Test
    public void testNowBindings() throws Exception {
        VariableSignal<Integer> a = signalFactory.var(7);
        TestBean bean = new TestBean();

        Map<String, Object> context = of("a", a, "bean", bean);
        Node<Object> tree = see.parseExpressionList("b = a.now(); bean.value <- b + 2;");
        see.evaluate(tree, context);

        assertEquals("9.0", bean.getValue().toString());

        a.set(42);
        assertEquals("9.0", bean.getValue().toString());
    }

    @Test
    public void testImplicitSignalCreation() throws Exception {
        VariableSignal<Integer> a = signalFactory.var(4);
        TestBean bean = new TestBean();

        Map<String, Object> context = of("a", a, "bean", bean);
        see.eval("bean.value <- a() + 5", context); // No signal() function

        assertEquals("9.0", bean.getValue().toString());
        
        a.set(37);
        assertEquals("42.0", bean.getValue().toString());
    }

    @Test
    public void testEagerness() throws Exception {
        VariableSignal<Boolean> a = signalFactory.var(false);
        VariableSignal<Boolean> b = signalFactory.var(false);

        Map<String, Object> context = ImmutableMap.<String, Object>of("a", a, "b", b);
        Signal<Boolean> result = (Signal<Boolean>) see.eval("signal(a() && b())", context); // b is not evaluated normally

        assertFalse(result.now());
        
        a.set(true);
        assertFalse(result.now());
        
        b.set(true);
        assertTrue(result.now()); // compound signal should react to changes of b
    }

    @Test
    public void testNullDuringDependencyResolution() throws Exception {
        VariableSignal<String> a = signalFactory.var(null);

        Map<String, Object> context = ImmutableMap.<String, Object>of("a", a);
        Signal<?> result = (Signal<?>) see.eval("signal(a() == null)", context);
        assertEquals(true, result.now());
        
        a.set("crn");
        assertEquals(false, result.now());
    }

    @Test
    public void testErrorsDuringDependencyResolution() throws Exception {
        VariableSignal<String> s = signalFactory.var(null);

        Map<String, Object> context = ImmutableMap.<String, Object>of("s", s);
        Signal<?> result = (Signal<?>) see.eval("signal(if(s() != null, s().class, 'empty'))", context);

        assertEquals("empty", result.now());

        s.set("crn");
        assertEquals(String.class, result.now());
    }

    public static class TestBean {
        private Number value;

        public Number getValue() {
            return value;
        }

        public void setValue(Number value) {
            this.value = value;
        }
    }
}
