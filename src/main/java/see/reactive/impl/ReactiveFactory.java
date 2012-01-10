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
import see.reactive.SignalFactory;
import see.reactive.VariableSignal;

import javax.annotation.Nonnull;
import java.util.Collection;

public class ReactiveFactory implements SignalFactory {
    private final EventBus eventBus = new EventBus();

    @Nonnull
    @Override
    public <T> VariableSignal<T> var(T initialValue) {
        return new Var<T>(eventBus, initialValue);
    }

    @Nonnull
    @Override
    public <T> Signal<T> bind(@Nonnull Collection<? extends Signal<?>> dependencies, @Nonnull Supplier<T> evaluation) {
        return new StatefulSignal<T>(eventBus, dependencies, evaluation);
    }

}
