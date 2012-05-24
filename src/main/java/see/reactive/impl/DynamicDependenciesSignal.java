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

import com.google.common.base.Supplier;
import see.reactive.EvaluationResult;
import see.reactive.Signal;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.all;

class DynamicDependenciesSignal<T> extends AbstractOrderedSignal<T> {

    private final Supplier<EvaluationResult<T>> evaluation;

    private DynamicDependenciesSignal(Supplier<EvaluationResult<T>> evaluation, Collection<? extends AbstractOrderedSignal<?>> dependencies, T initialValue) {
        super(dependencies, initialValue);
        this.evaluation = evaluation;
    }

    public static <T> DynamicDependenciesSignal<T> create(Supplier<EvaluationResult<T>> evaluation) {
        EvaluationResult<T> result = evaluation.get();
        return new DynamicDependenciesSignal<T>(evaluation, checkDependencies(result.getDependencies()), result.getResult());
    }

    private static Collection<? extends AbstractOrderedSignal<?>> checkDependencies(Collection<? extends Signal<?>> deps) {
        checkArgument(all(deps, instanceOf(AbstractOrderedSignal.class)));
        return (Collection<? extends AbstractOrderedSignal<?>>) deps;
    }

    @Override
    protected T evaluate() {
        EvaluationResult<T> result = evaluation.get();
        setDependencies(result.getDependencies());
        return result.getResult();
    }
}
