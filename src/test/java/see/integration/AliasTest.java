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
import see.See;

import static org.junit.Assert.assertEquals;
import static see.parser.config.ConfigBuilder.defaultConfig;

public class AliasTest {
    @Test
    public void testFunctionAliases() throws Exception {
        See aliased = new See(defaultConfig().addAlias("cond", "if").build());

        assertEquals("ok", aliased.eval("cond(true, 'ok', 'wrong')"));
    }
}
