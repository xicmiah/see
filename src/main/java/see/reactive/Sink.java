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

import com.google.common.eventbus.EventBus;

import java.util.Collection;

public class Sink extends AbstractDependency {

    private final Runnable actions;

    public Sink(EventBus eventBus, Collection<? extends Dependency> dependencies, Runnable actions) {
        super(eventBus, dependencies);
        this.actions = actions;
    }

    @Override
    protected void updateInternalState() {
        actions.run();
        invalidate();
    }
}
