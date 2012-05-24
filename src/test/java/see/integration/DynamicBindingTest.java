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
import see.functions.reactive.MakeDynamicSignal;
import see.parser.config.ConfigBuilder;
import see.reactive.Signal;
import see.reactive.VariableSignal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;

public class DynamicBindingTest extends BindingTest {
    @Override
    protected ReactiveSee createReactiveSee() {
        return new ReactiveSee(ConfigBuilder.defaultConfig()
                .addFunction("signal", new MakeDynamicSignal())
                .build());
    }

    @Nonnull
    private  <T> VariableSignal<T> var(@Nullable T initialValue) {
        return signalFactory.var(initialValue);
    }

    @Test
    public void testDynamicBinding() throws Exception {
        VariableSignal<String> a = var("a");
        VariableSignal<String> b = var("b");
        VariableSignal<String> selector = var("a");

        Map<String, Object> context = of("signals", of("a", a, "b", b), "selector", selector);
        Signal<?> result = (Signal<?>) see.eval("signal(signals[selector()]())", context); // b is never evaluated

        assertEquals("a", result.now());

        a.set("a1");
        assertEquals("a1", result.now());

        selector.set("b");
        assertEquals("b", result.now());

        b.set("b1");
        assertEquals("b1", result.now());
    }
}
