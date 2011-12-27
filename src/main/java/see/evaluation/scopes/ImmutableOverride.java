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

import javax.annotation.Nonnull;
import java.util.Map;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Maps.filterKeys;

class ImmutableOverride implements Scope {
    
    private final Scope parent;
    
    private final Map<String, Object> overrides;

    ImmutableOverride(Scope parent, Map<String, ?> overrides) {
        this.parent = parent;
        this.overrides = ImmutableMap.copyOf(overrides);
    }

    @Override
    public Object get(@Nonnull String var) {
        if (overrides.containsKey(var)) {
            return overrides.get(var);
        } else {
            return parent.get(var);
        }
    }

    @Override
    public boolean contains(@Nonnull String var) {
        return overrides.containsKey(var) || parent.contains(var);
    }

    @Override
    public void put(@Nonnull String var, Object value) {
        if (overrides.containsKey(var)) {
            throw new UnsupportedOperationException("Scope override is immutable");
        } else {
            parent.put(var, value);
        }
    }

    @Nonnull
    @Override
    public Map<String, ?> asMap() {
        return ImmutableMap.<String, Object>builder()
                .putAll(filterKeys(parent.asMap(), not(in(overrides.keySet()))))
                .putAll(overrides)
                .build();
    }
}
