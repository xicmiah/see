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

import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Collection;

public abstract class AbstractDependency implements Dependency {
    private final EventBus eventBus;

    private Collection<? extends Dependency> dependencies;

    protected AbstractDependency(EventBus eventBus, Collection<? extends Dependency> dependencies) {
        this.eventBus = eventBus;
        this.dependencies = dependencies;
        eventBus.register(this);
    }

    protected void invalidate() {
        eventBus.post(new ChangeEvent(this));
    }

    @Subscribe
    public void handleDependencyChange(ChangeEvent changeEvent) {
        if (Iterables.contains(dependencies, changeEvent.getTarget())) {
            updateInvalidate();
        }
    }

    @Override
    public Collection<? extends Dependency> getDependencies() {
        return dependencies;
    }

    protected abstract void updateInvalidate();
}
