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
import see.evaluation.Scope;

import java.util.Map;

/**
 * Factory methods for {@link Scope} instances
 */
public abstract class Scopes {
    private Scopes() {}

    /**
     * Return empty immutable scope.
     * @return empty, immutable scope.
     */
    public static Scope empty() {
        return new ImmutableScope(ImmutableMap.<String, Object>of());
    }

    /**
     * Create new immutable scope from supplied {@link Map}.
     * @param vars initial variables in scope
     * @return created scope
     */
    public static Scope fromMap(Map<String, ?> vars) {
        return new ImmutableScope(vars);
    }

    /**
     * Create new scope, with some variables overridden.
     * {@link Scope#get(String)} calls for such variables will return new values.
     * {@link Scope#put(String, Object)} calls will fail for new variables.
     * Calls for non-overridden variables will be forwarded to parent scope.
     * @param parent parent scope
     * @param overriddenVars variables to override
     * @return created scope
     */
    public static Scope override(Scope parent, Map<String, ?> overriddenVars) {
        return new ImmutableOverride(parent, overriddenVars);
    }

    /**
     * Create new scope, with some variables overridden.
     * {@link Scope#get(String)} calls for such variables will return new values.
     * {@link Scope#put(String, Object)} calls will set only new variables.
     * Calls for non-overridden variables will be forwarded to parent scope.
     * @param parent parent scope
     * @param overriddenVars variables to override
     * @return created scope
     */
    public static Scope mutableOverride(Scope parent, Map<String, ?> overriddenVars) {
        return new MutableOverride(parent, overriddenVars);
    }

    /**
     * Create new mutable scope, which captures new variables.
     * Get/put operations for variables existent in parent scope are delegated there.
     * @param parent parent scope
     * @return created scope
     */
    public static Scope defCapture(Scope parent) {
        return new DefinitionCapture(parent);
    }
}
