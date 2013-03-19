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

package see.evaluation;

import see.exceptions.NoSuchVariableException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Hierarchical {@link Map}-like container.
 */
public interface Scope {
    /**
     * Get variable value by it's name.
     * @param var variable name
     * @return variable value
     * @throws NoSuchVariableException if variable wasn't found
     */
    Object get(@Nonnull String var) throws NoSuchVariableException;

    /**
     * Return, if variable with such value exists.
     * @param var variable name
     * @return true, if such variable exists
     */
    boolean contains(@Nonnull String var);

    /**
     * Set variable to given value
     * @param var variable name
     * @param value new variable value
     */
    void put(@Nonnull String var, @Nullable Object value);

    /**
     * Get read-only snapshot of scope contents
     * @return scope contents
     */
    @Nonnull
    Map<String, ?> asMap();
}
