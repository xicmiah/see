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

import com.google.common.base.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface SignalFactory {
    /**
     * Create a variable endpoint - signal without dependencies, can be updated.
     * @param initialValue initial signal value
     * @param <T> value type
     * @return variable signal
     */
    @Nonnull
    <T> VariableSignal<T> var(@Nullable T initialValue);

    /**
     * Bind expression evaluation to specified dependencies.
     * Evaluation will be triggered on dependency change.
     * Then, if result is different, this signal will be invalidated.
     * @param dependencies expression dependencies
     * @param evaluation expression
     * @param <T> expression return type
     * @return constructed signal
     */
    @Nonnull
    <T> Signal<T> bind(@Nonnull Collection<? extends Signal<?>> dependencies, @Nonnull Supplier<T> evaluation);
}
