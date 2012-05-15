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

package see.properties.impl;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Iterables.unmodifiableIterable;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static see.parser.grammar.PropertyAccess.indexed;

public class IterableResolverTest {

    IterableResolver resolver = new IterableResolver();

    @Test
    public void testCanGet() throws Exception {
        assertTrue(resolver.canGet(newArrayList(), indexed(2)));
        assertTrue(resolver.canGet(newArrayList(), indexed(new BigDecimal(2))));

        assertTrue(resolver.canGet(newHashSet(), indexed(2)));
        assertTrue(resolver.canGet(unmodifiableIterable(newHashSet()), indexed(2)));
    }

    @Test
    public void testCanSet() throws Exception {
        assertTrue(resolver.canSet(newArrayList(), indexed(2), "c"));
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(2, resolver.get(asList(1, 2, 3), indexed(1)));
        assertEquals(2, resolver.get(of(1, 2, 3), indexed(1)));
        assertEquals(2, resolver.get(unmodifiableIterable(asList(1, 2, 3)), indexed(1)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutsideRange() throws Exception {
        resolver.get(of(1, 2, 3), indexed(42));
    }

    @Test
    public void testSet() throws Exception {
        List<?> list = newArrayList(1, 2, 3);

        resolver.set(list, indexed(1), 42);
        assertEquals(newArrayList(1, 42, 3), list);
    }
}
