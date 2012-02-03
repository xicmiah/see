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

package see.functions.collections;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import see.See;
import see.functions.functional.Filter;
import see.functions.functional.FlatMap;
import see.functions.functional.FoldFunction;
import see.functions.functional.Transform;
import see.parser.config.ConfigBuilder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Iterables.concat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CollectionTransformationsTest {
    See see = new See(ConfigBuilder.defaultConfig()
            .addFunction("map", new Transform())
            .addFunction("filter", new Filter())
            .addFunction("flatMap", new FlatMap())
            .addFunction("fold", new FoldFunction())
            .build());

    @Test
    public void testMap() throws Exception {
        Object result = see.eval("map([1, 2, 3], function(a) { a.toString(); })");
        assertEquals(of("1", "2", "3"), result);
    }

    @Test
    public void testFilter() throws Exception {
        Object result = see.eval("filter([9, 42, 100500], function(x) { x < 10; })");
        assertEquals(of(new BigDecimal("9")), copyOf((Iterable<?>) result));
    }
    @Test
    public void testTransformCollectionType() throws Exception {
        assertThat(doMap(of(1, 2, 3)), instanceOf(List.class));
        assertThat(doMap(ImmutableSet.of(1, 2, 3)), allOf(instanceOf(Collection.class), not(instanceOf(List.class))));
        assertThat(doMap(concat(of(1), of(2), of(3))), allOf(instanceOf(Iterable.class), not(instanceOf(Collection.class))));
    }

    private Object doMap(Iterable<?> data) {
        return see.eval("map(data, function(x) { x.toString(); })", ImmutableMap.of("data", data));
    }

    @Test
    public void testFilterCollectionType() throws Exception {
        assertThat(doFilter(ImmutableSet.of(1, 2, 3)), instanceOf(Set.class));
        assertThat(doFilter(of(1, 2, 3)), allOf(instanceOf(Collection.class), not(instanceOf(Set.class))));
        assertThat(doFilter(concat(of(1), of(2), of(3))), allOf(instanceOf(Iterable.class), not(instanceOf(Collection.class))));
    }

    private Object doFilter(Iterable<?> data) {
        return see.eval("filter(data, function(x) { x < 10; })", ImmutableMap.of("data", data));
    }

    @Test
    public void testFlatMap() throws Exception {
        Object result = see.eval("flatMap([9, 42, 100500], function(x) { if(x < 10, [x.toString()], []); })");
        assertEquals(of("9"), copyOf((Iterable<?>) result));
    }

    @Test
    public void testFold() throws Exception {
        Object result = see.eval("fold('', ['c', 'r', 'n'], function(a,b) { a + b; })");
        assertEquals("crn", result);
    }
}
