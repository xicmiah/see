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

import com.google.common.collect.Maps;
import org.junit.Test;
import see.See;
import see.functions.VarArgFunction;
import see.functions.service.MakeFunction;
import see.tree.Node;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static see.parser.config.ConfigBuilder.defaultConfig;

public class FunctionCreationTest {
    See see = new See(defaultConfig().addFunction("def", new MakeFunction()).build());

    @Test
    public void testReturnType() throws Exception {
        Object result = see.eval("def(args, @tree 1)", of("args", asList("a", "b")));
        assertThat(result, instanceOf(VarArgFunction.class));
    }

    @Test
    public void testResultEvaluation() throws Exception {
        Node<Object> tree = see.parseExpressionList("s = def(args, @tree a + b); s('crn', 'bka');");

        Object result = see.evaluate(tree, Maps.newHashMap(of("args", asList("a", "b"))));
        assertEquals(result, "crn" + "bka");
    }
}
