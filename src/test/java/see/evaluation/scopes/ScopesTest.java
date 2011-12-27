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

package see.evaluation.scopes;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import see.evaluation.Scope;

import java.util.NoSuchElementException;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScopesTest {

    @Test
    public void testEmptyScope() throws Exception {
        Scope empty = Scopes.empty();

        assertTrue(empty.asMap().isEmpty());
    }

    @Test
    public void testInitialScope() throws Exception {
        ImmutableMap<String, ?> initial = of("a", 9, "b", new Object());
        Scope scope = Scopes.fromMap(initial);

        assertTrue(scope.contains("a"));
        assertTrue(scope.contains("b"));
        assertEquals(9, scope.get("a"));
        assertEquals(initial, scope.asMap());
    }

    @Test(expected = NoSuchElementException.class)
    public void testAbsentVariables() throws Exception {
        Scope scope = Scopes.fromMap(of("a", 9));

        scope.get("b");
    }

    @Test
    public void testOverride() throws Exception {
        Scope initial = Scopes.fromMap(of("a", 5, "c", 4));
        Scope over = Scopes.override(initial, of("c", 9, "crn", "bka"));

        assertEquals(of("a", 5, "c", 9, "crn", "bka"), over.asMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutToOverridden() throws Exception {
        Scope scope = Scopes.override(Scopes.fromMap(of("a", 5)), of("c", 9));

        scope.put("c", "bka");
    }

    @Test
    public void testDefinitionCapture() throws Exception {
        Scope scope = Scopes.defCapture(Scopes.fromMap(of("c", 9)));

        scope.put("crn", "bka");
        assertEquals(of("c", 9, "crn", "bka"), scope.asMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDefinitionDelegation() throws Exception {
        Scope scope = Scopes.defCapture(Scopes.fromMap(of("c", 9)));

        scope.put("c", "bka");
    }

    @Test
    public void testPutDelegation() throws Exception {
        Scope mutable = Scopes.defCapture(Scopes.empty());
        Scope scope = Scopes.override(mutable, of("crn", "bka"));

        scope.put("c", 9);

        assertEquals(9, scope.get("c"));
        assertEquals(9, mutable.get("c"));
    }

    @Test
    public void testMutableOverrides() throws Exception {
        Scope initial = Scopes.fromMap(of("a", 5, "c", 4));
        Scope mutable = Scopes.mutableOverride(initial, of("c", 5, "crn", "crn"));

        mutable.put("c", 9);
        mutable.put("crn", "bka");

        assertEquals(of("a", 5, "c", 9, "crn", "bka"), mutable.asMap());
    }

    @Test
    public void testMutableDelegation() throws Exception {
        Scope mutable = Scopes.defCapture(Scopes.empty());
        Scope over = Scopes.mutableOverride(mutable, of("c", 9));

        over.put("cat", "fine too");
        assertEquals("fine too", mutable.get("cat"));
    }
}
