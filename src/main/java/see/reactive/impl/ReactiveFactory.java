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

package see.reactive.impl;

import com.google.common.base.Supplier;
import com.google.common.eventbus.EventBus;
import see.reactive.Signal;
import see.reactive.VariableSignal;

import java.util.Collection;

public class ReactiveFactory {
    private final EventBus eventBus = new EventBus();

    /**
     * Create a constant signal.
     * @param value signal value
     * @param <T> value type
     * @return constant signal
     */
    public <T> Signal<T> val(T value) {
        return new Val<T>(value);
    }

    /**
     * Create a variable endpoint - signal without dependencies, can be updated.
     * @param initialValue initial signal value
     * @param <T> value type
     * @return variable signal
     */
    public <T> VariableSignal<T> var(T initialValue) {
        return new Var<T>(eventBus, initialValue);
    }

    /**
     * Bind expression evaluation to specified dependencies.
     * Evaluation will be triggered on dependency change.
     * Then, if result is different, this signal will be invalidated.
     * @param dependencies expression dependencies
     * @param evaluation expression
     * @param <T> expression return type
     * @return constructed signal
     */
    public <T> Signal<T> bind(Collection<? extends Signal<?>> dependencies, Supplier<T> evaluation) {
        return new StatefulSignal<T>(eventBus, dependencies, evaluation);
    }

    /**
     * Bind expression evaluation to specified dependencies.
     * Evaluation will be triggered on {@code Signal.getNow()} call.
     * Invalidation will be triggered on dependency change.
     * @param dependencies expression dependencies
     * @param evaluation expression
     * @param <T> expression return type
     * @return constructed signal
     */
    public <T> Signal<T> bindLazy(Collection<? extends Signal<?>> dependencies, Supplier<T> evaluation) {
        return new StatelessSignal<T>(eventBus, dependencies, evaluation);
    }
}
