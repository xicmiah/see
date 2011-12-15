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

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import see.reactive.Signal;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

abstract class AbstractSignal<T> implements Signal<T> {
    private final EventBus eventBus;

    private final Set<? extends Signal<?>> dependencies;

    protected AbstractSignal(EventBus eventBus, Collection<? extends Signal<?>> dependencies) {
        this.eventBus = eventBus;
        this.dependencies = ImmutableSet.copyOf(dependencies);
        eventBus.register(this);
    }

    protected final void invalidate() {
        eventBus.post(new ChangeEvent(this));
    }

    @Subscribe
    public void handleDependencyChange(ChangeEvent changeEvent) {
        if (dependencies.contains(changeEvent.getTarget())) {
            updateInternalState();
        }
    }

    protected abstract void updateInternalState();

    /**
     * Stub implementation which always throws exception.
     * Intended to be intercepted in signal expressions.
     * @param input empty arg list
     * @return never returns
     * @throws IllegalStateException always
     */
    @Override
    public T apply(@Nonnull List<Void> input) {
        throw new IllegalStateException("Can be called only from signal expressions");
    }

    private static class ChangeEvent {
        private final Signal<?> target;

        public ChangeEvent(Signal<?> target) {
            this.target = target;
        }

        public Signal<?> getTarget() {
            return target;
        }
    }
}
