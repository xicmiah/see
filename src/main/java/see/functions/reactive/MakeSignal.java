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

package see.functions.reactive;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import see.reactive.Dependency;
import see.reactive.Signal;
import see.reactive.impl.ReactiveFactory;
import see.tree.Node;

import java.util.Collection;
import java.util.List;

public class MakeSignal extends ReactiveFunction<Object, Signal<?>> {
    @Override
    public Signal<?> apply(ReactiveFactory factory, final List<Object> input) {
        Preconditions.checkArgument(input.size() == 2, "MakeSignal takes two arguments");

        Supplier<Object> evaluation = new Supplier<Object>() {
            @Override
            public Object get() {
                return input.get(0);
            }
        };
        Node<Object> tree = (Node<Object>) input.get(1);
        Collection<Dependency> dependencies = getDependencies(tree);

        return factory.bind(dependencies, evaluation);
    }

    protected Collection<Dependency> getDependencies(Node<Object> tree) {
        // TODO: implement proper dependency lookup
        return ImmutableSet.of();
    }

    @Override
    public String toString() {
        return "signal";
    }
}
