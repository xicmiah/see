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
import see.See;
import see.tree.Node;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

public class IterationTest {
    See see = new See();

    final List<?> testList = of(new StringBean("nine"), new StringBean("crno"), new StringBean("bka"));
    final Map<String, ?> baseContext = ImmutableMap.of("list", testList);

    @Test
    public void testIteration() throws Exception {
        Map<String, ?> context = newHashMap(baseContext);

        Node<?> tree = see.parseExpressionList("a = 0; for(s : list) a = a + s.length;");
        see.evaluate(tree, context);

        assertEquals("11.0", context.get("a").toString());
    }
    
    public static class StringBean {
        private String content;

        public StringBean(String content) {
            this.content = content;
        }
        
        public int getLength() {
            return content.length();
        }
    }
}
