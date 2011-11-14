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
import org.junit.Test;
import see.ReactiveSee;
import see.reactive.VariableSignal;
import see.reactive.impl.ReactiveFactory;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BindingTest {
    ReactiveFactory reactiveFactory = new ReactiveFactory();
    ReactiveSee see = new ReactiveSee(reactiveFactory);

    @Test
    public void testBindings() throws Exception {
        VariableSignal<String> var = reactiveFactory.var("crno");
        PropertyTraversalTest.TestBean bean = new PropertyTraversalTest.TestBean("omg", null);

        Map<String, Object> context = ImmutableMap.of("a", bean, "v", var);
        
        see.eval("a.name <- v", context);
        assertEquals("crno", bean.getName());

        var.update("bka");
        assertEquals("bka", bean.getName());
    }
}
