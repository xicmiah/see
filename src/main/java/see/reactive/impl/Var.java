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

package see.reactive.impl;

import com.google.common.collect.ImmutableSet;
import see.reactive.VariableSignal;

class Var<T> extends AbstractOrderedSignal<T> implements VariableSignal<T> {

    /**
     * Separate current value required for invalidation logic.
     * Call to {@link see.reactive.impl.AbstractOrderedSignal#invalidate()} will sync with superclass.
     */
    private T localValue;

    public Var(T value) {
        super(ImmutableSet.<AbstractOrderedSignal<?>>of(), value);
        this.localValue = value;
    }

    @Override
    protected T evaluate() {
        return localValue;
    }

    @Override
    public void set(T value) {
        this.localValue = value;
        invalidate();
    }
}
