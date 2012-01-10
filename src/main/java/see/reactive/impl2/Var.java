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

package see.reactive.impl2;

import com.google.common.collect.ImmutableSet;
import see.reactive.VariableSignal;

public class Var<T> extends AbstractObserverSignal<T> implements VariableSignal<T> {

    public Var(T initialValue) {
        super(ImmutableSet.<AbstractObserverSignal<?>>of(), initialValue);
    }

    @Override
    protected void handleChangeEvent() {
        // No dependencies, won't be called
    }

    @Override
    public void set(T value) {
        fireChangeEvent(value);
    }
}
