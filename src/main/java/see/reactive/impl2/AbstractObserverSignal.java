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

import see.reactive.Signal;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.all;

public abstract class AbstractObserverSignal<T> implements Signal<T> {
    
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    
    private T currentValue;

    protected AbstractObserverSignal(Collection<? extends Signal<?>> dependencies, T initialValue) {
        checkArgument(all(dependencies, instanceOf(AbstractObserverSignal.class)),
                "All dependant signals should be created by same SignalFactory implementation");

        this.currentValue = initialValue;

        @SuppressWarnings("unchecked")
        Collection<AbstractObserverSignal<?>> castedDeps = (Collection<AbstractObserverSignal<?>>) dependencies;

        addListeners(castedDeps);
    }

    private void addListeners(Collection<? extends AbstractObserverSignal<?>> dependencies) {
        for (AbstractObserverSignal<?> dependency : dependencies) {
            dependency.changeSupport.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    handleChangeEvent();
                }
            });
        }
    }

    protected void fireChangeEvent(T newValue) {
        changeSupport.firePropertyChange("currentValue", currentValue, currentValue = newValue);
    }

    protected abstract void handleChangeEvent();

    @Override
    public T now() {
        return currentValue;
    }

    @Override
    public T apply(@Nonnull List<Void> input) throws IllegalStateException {
        throw new IllegalStateException("Can be called only from signal expressions");
    }
}
