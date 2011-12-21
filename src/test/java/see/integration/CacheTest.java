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
import see.functions.opt.Memoize;

import java.math.BigDecimal;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static see.parser.config.ConfigBuilder.defaultConfig;

public class CacheTest {
    See see = new See(defaultConfig()
            .addPureFunction("memoize", Memoize.memoizeFunction())
            .build()
    );

    @Test
    public void testCache() throws Exception {
//        Object f = see.eval("fib = function(n) { if(n == 0 || n == 1, n, fib(n-2) + fib(n-1)); }");
        Object f = see.eval("fib = memoize(function(n) { if(n == 0 || n == 1, n, fib(n-2) + fib(n-1)); })");
        Object result = see.eval("fib(20)", of("fib", f));
        assertEquals(new BigDecimal(6765), result);
    }
}
