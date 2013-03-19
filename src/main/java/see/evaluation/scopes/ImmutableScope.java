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
import com.google.common.collect.ImmutableSet;
import see.evaluation.Scope;
import see.exceptions.NoSuchVariableException;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Predicates.isNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Maps.filterValues;

class ImmutableScope implements Scope {

    // Store immutable map + set of null keys
    private final Map<String, Object> contents;
    private final Collection<String> nullKeys;

    ImmutableScope(Map<String, ?> contents) {
        if (contents instanceof ImmutableMap<?, ?>) {
            this.contents = copyOf(contents);
            this.nullKeys = of();
        } else {
            this.contents = copyOf(filterValues(contents, notNull()));
            this.nullKeys = ImmutableSet.copyOf(filterValues(contents, isNull()).keySet());
        }
    }

    @Override
    public Object get(@Nonnull String var) {
        if (contains(var)) {
            return contents.get(var);
        } else {
            throw new NoSuchVariableException(var);
        }
    }

    @Override
    public void put(@Nonnull String var, Object value) {
        throw new UnsupportedOperationException("Scope is immutable");
    }

    @Override
    public boolean contains(@Nonnull String var) {
        return contents.containsKey(var) || nullKeys.contains(var);
    }

    @Nonnull
    @Override
    public Map<String, ?> asMap() {
        return contents;
    }
}
