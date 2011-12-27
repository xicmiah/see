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
import java.util.NoSuchElementException;

class ImmutableScope implements Scope {

    private final Map<String, Object> contents;

    ImmutableScope(Map<String, ?> contents) {
        this.contents = ImmutableMap.copyOf(contents);
    }

    @Override
    public Object get(@Nonnull String var) {
        if (contents.containsKey(var)) {
            return contents.get(var);
        } else {
            throw new NoSuchElementException("Cannot resolve variable " + var);
        }
    }

    @Override
    public void put(@Nonnull String var, Object value) {
        throw new UnsupportedOperationException("Scope is immutable");
    }

    @Override
    public boolean contains(@Nonnull String var) {
        return contents.containsKey(var);
    }

    @Nonnull
    @Override
    public Map<String, ?> asMap() {
        return contents;
    }
}
