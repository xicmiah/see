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

package see.reactive;

import java.util.Collection;

/**
 * Result of expression evaluation with dependency collection
 * @param <T> expression result type
 */
public class EvaluationResult<T> {

    private final T result;
    private final Collection<? extends Signal<?>> dependencies;

    public EvaluationResult(T result, Collection<? extends Signal<?>> dependencies) {
        this.result = result;
        this.dependencies = dependencies;
    }

    /**
     * Get expression result
     * @return result
     */
    public T getResult() {
        return result;
    }

    /**
     * Get collected dependencies
     * @return dependencies
     */
    public Collection<? extends Signal<?>> getDependencies() {
        return dependencies;
    }
}
