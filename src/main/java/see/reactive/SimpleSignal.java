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

import com.google.common.base.Supplier;

public class SimpleSignal<T> extends AbstractDependency implements Signal<T> {

    private Supplier<T> evaluation;

    public SimpleSignal(Iterable<? extends Dependency> dependencies, Supplier<T> evaluation) {
        super(dependencies);
        this.evaluation = evaluation;
    }

    @Override
    public T now() {
        return evaluation.get();
    }

    @Override
    protected void updateInvalidate() {
        // No internal state
    }
}
