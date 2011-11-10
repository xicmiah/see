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

package see.reactive;

import com.google.common.base.Supplier;
import com.google.common.eventbus.EventBus;

import java.util.Collection;

public class ReactiveFactory {
    private EventBus eventBus = new EventBus();

    public <T> Signal<T> bind(Collection<? extends Dependency> dependencies, Supplier<T> evaluation) {
        return new SimpleSignal<T>(dependencies, evaluation);
    }

    public <T> Signal<T> bindWithState(Collection<? extends Dependency> dependencies, Supplier<T> evaluation) {
        return new StatefulSignal<T>(dependencies, evaluation);
    }

    public <T> VariableSignal<T> var(T initialValue) {
        return new Var<T>(initialValue);
    }

    public Dependency sink(Collection<? extends Dependency> dependencies, Runnable actions) {
        return new Sink(dependencies, actions);
    }
}
