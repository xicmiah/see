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

import com.google.common.base.Preconditions;
import see.reactive.Signal;

import static com.google.common.collect.ImmutableSet.of;

/**
 * Signal, which delegates to another signal.
 * Delegate reference is mutable.
 * @param <T> signal type
 */
class DelegatingSignal<T> extends AbstractObserverSignal<T> {

    private AbstractObserverSignal<T> delegate;

    public DelegatingSignal(Signal<T> delegate) {
        super(of(delegate), delegate.now());
        this.delegate = (AbstractObserverSignal<T>) delegate;
    }

    @Override
    protected void handleChangeEvent() {
        fireChangeEvent(delegate.now());
    }

    public Signal<T> getDelegate() {
        return delegate;
    }

    public void setDelegate(Signal<T> delegate) {
        Preconditions.checkArgument(delegate instanceof AbstractObserverSignal<?>);

        unsubscribe(this.delegate);
        this.delegate = (AbstractObserverSignal<T>) delegate;
        subscribe(this.delegate);

        fireChangeEvent(delegate.now());
    }
}
