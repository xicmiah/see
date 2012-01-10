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

package see.functions.reactive;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import see.ReactiveSee;
import see.parser.config.ConfigBuilder;
import see.parser.grammar.PropertyAccess;
import see.properties.impl.PropertyUtilsResolver;
import see.reactive.SignalFactory;
import see.reactive.VariableSignal;
import see.reactive.impl.ReactiveFactory;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SignalDecoratorTest {
    SignalResolver signalResolver;
    SignalFactory signalFactory;
    ReactiveSee see;

    private Cache<SimpleBean, VariableSignal<Object>> signals = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<SimpleBean, VariableSignal<Object>>() {
        @Override
        public VariableSignal<Object> load(SimpleBean key) throws Exception {
            return signalFactory.<Object>var(key.getName());
        }
    });


    @Before
    public void setUp() throws Exception {
        ConfigBuilder config = ConfigBuilder.defaultConfig();
        signalResolver = new SignalResolver() {
            @Override
            public Optional<VariableSignal<Object>> getSignal(Object target, PropertyAccess lastProperty) {
                for (String property : lastProperty.value().left()) {
                    if ("name".equals(property) && target instanceof SimpleBean) {
                        return Optional.of(signals.getUnchecked((SimpleBean) target));
                    }
                }
                return Optional.absent();
            }
        };

        config.setPropertyResolver(new SignalDecorator(new PropertyUtilsResolver(), signalResolver));
        signalFactory = new ReactiveFactory();
        see = new ReactiveSee(config.build(), signalFactory);
    }

    @Test
    public void testParallelSignals() throws Exception {
        SimpleBean bean = new SimpleBean("crn");

        assertBeanContract(bean);

        evalOn("bean.name = 'bka'", bean);
        assertEquals("bka", bean.getName());
        assertBeanContract(bean);
    }

    /**
     * Assert equality of SimpleBean name and corresponding signal value
     * @param bean bean to check
     */
    private void assertBeanContract(SimpleBean bean) {
        assertEquals(bean.getName(), signals.getUnchecked(bean).now());
    }

    private void evalOn(String expression, SimpleBean bean) {
        Map<String, Object> context = ImmutableMap.<String, Object>of("bean", bean);
        see.eval(expression, context);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static class SimpleBean {
        private String name;

        public SimpleBean() {
        }

        public SimpleBean(String name) {
            this.name = name;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
