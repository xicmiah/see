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
import com.google.common.collect.ImmutableMap;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import see.See;
import see.functions.VarArgFunction;

import static org.junit.Assert.*;

public class MethodCallsTest {
    See see = new See();

    @Test
    public void testNoArgs() throws Exception {
        Object result = see.eval("'crn'.getClass()");
        assertEquals(String.class, result);
    }

    @Test
    public void testArgs() throws Exception {
        Object result = see.eval("'crn'.replace('crn', 'bka')");
        assertEquals("bka", result);
    }

    @Test
    public void testVoid() throws Exception {
        Object doNothing = new Runnable() {
            @Override
            public void run() {
            }
        };
        Object result = see.eval("r.run()", ImmutableMap.of("r", doNothing));

        assertNull(result);
    }

    @Test
    public void testClosures() throws Exception {
        Object result = see.eval("'crn'.getClass"); // No parenthesis, returns a closure ...
        assertThat(result, IsInstanceOf.instanceOf(VarArgFunction.class));

        VarArgFunction<Object, Object> closure = (VarArgFunction<Object, Object>) result;
        Object closureResult = closure.apply(ImmutableList.of()); // ... which can be evaluated manually ...
        assertEquals(String.class, closureResult);
    }

    @Test
    public void testClosureEvaluation() throws Exception {
        Object result = see.eval("apply('crn'.concat, 'bka')"); // ... or via apply(f, args...) function
        assertEquals("crn".concat("bka"), result);
    }

    @Test
    public void testApplyShortcut() throws Exception {
        Object args = ImmutableList.of("bka");
        ImmutableMap<String, Object> context = ImmutableMap.of("args", args);
        Object result = see.eval("'crn'.concat.apply(args)", context); // ... or via .apply(args) method
        assertEquals("crn".concat("bka"), result);
    }
}
