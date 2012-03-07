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

package see.functions.functional;

import com.google.common.collect.Maps;
import org.junit.Test;
import see.See;
import see.parser.config.ConfigBuilder;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionFunctionTest {
    See see = new See(ConfigBuilder.defaultConfig()
            .addFunction("option", new OptionFunction())
            .build());

    @Test
    public void testOptions() throws Exception {
        assertEquals(BigDecimal.valueOf(6.0), evalOption(5));
        assertNull(evalOption(null));
    }

    private Object evalOption(@Nullable Object value) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("a", value);
        return see.eval("option(a).map(function(a) { a + 1; }).get()", context);
    }
}
