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

package see.reactive.impl3;

import com.google.common.collect.Sets;
import see.reactive.Signal;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Objects.equal;
import static com.google.common.collect.ImmutableSet.of;

/**
 * Signal implementation, where changes are propagated in topological order,
 * i.e. signal is guaranteed to be re-evaluated after it's dependencies.
 * @param <T> signal value type
 */
public abstract class AbstractOrderedSignal<T> implements Signal<T>, Comparable<AbstractOrderedSignal<?>> {
    /**
     * Signal level for topological order.
     * Sources have level zero, bound signals have max(dependencies levels)+1.
     */
    private final int level;
    private T currentValue;
    private final Collection<AbstractOrderedSignal<?>> dependants = Sets.newHashSet();

    protected AbstractOrderedSignal(Collection<? extends AbstractOrderedSignal<?>> dependencies, T initialValue) {
        this.currentValue = initialValue;
        this.level = dependencies.isEmpty() ? 0 : Collections.max(dependencies).level + 1;
        subscribeDependants(dependencies);
    }

    @Override
    public T apply(@Nonnull List<Void> input) throws IllegalStateException {
        throw new IllegalStateException("Can be called only from signal expressions");
    }

    private void subscribeDependants(Collection<? extends AbstractOrderedSignal<?>> dependencies) {
        for (AbstractOrderedSignal<?> dependency : dependencies) {
            dependency.subscribe(this);
        }
    }

    protected void subscribe(AbstractOrderedSignal<?> dependant) {
        dependants.add(dependant);
    }

    protected void unsubscribe(AbstractOrderedSignal<?> dependant) {
        dependants.remove(this);
    }

    @Override
    public int compareTo(AbstractOrderedSignal<?> o) {
        return Integer.valueOf(this.level).compareTo(o.level);
    }

    @Override
    public T now() {
        return currentValue;
    }

    /**
     * Re-evaluate this signal
     * @return new signal value
     */
    protected abstract T evaluate();

    protected void invalidate() {
        propagateChanges(of(this));
    }

    private boolean reEvalAndCheck() {
        return updateAndCheck(evaluate());
    }

    private boolean updateAndCheck(T newValue) {
        return !equal(currentValue, currentValue = newValue);
    }

    private void propagateChanges(Collection<? extends AbstractOrderedSignal<?>> initial) {
        Queue<AbstractOrderedSignal<?>> toUpdate = new PriorityQueue<AbstractOrderedSignal<?>>(initial);

        while (!toUpdate.isEmpty()) {
            AbstractOrderedSignal<?> dependant = toUpdate.poll();
            boolean doPropagate = dependant.reEvalAndCheck();
            if (doPropagate) toUpdate.addAll(dependant.dependants);
        }
    }
}
