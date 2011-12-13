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

package see.evaluation.visitors;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import see.evaluation.Context;
import see.evaluation.ValueProcessor;
import see.properties.ChainResolver;
import see.tree.Node;

import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.transform;

/**
 * Visitor, which evaluates function arguments eagerly.
 */
public class EagerVisitor extends AbstractVisitor {
    public EagerVisitor(Context context, ValueProcessor valueProcessor, ChainResolver resolver) {
        super(context, valueProcessor, resolver);
    }

    /**
     * Returns evaluated arguments.
     * Calling {@link List#get(int)} on returned list will either return pre-evaluated result,
     * or throw an exception.
     * @param arguments argument nodes to evaluate
     * @param <Arg> common argument supertype
     * @return pre-evaluated arguments
     */
    @Override
    protected <Arg> List<Arg> evaluateArgs(List<Node<Arg>> arguments) {
        List<Supplier<Arg>> evaluated = copyOf(transform(arguments, new Function<Node<Arg>, Supplier<Arg>>() {
            @Override
            public Supplier<Arg> apply(Node<Arg> input) {
                try {
                    return Suppliers.ofInstance(input.accept(EagerVisitor.this));
                } catch (RuntimeException e) {
                    return new ThrowingSupplier<Arg>(e);
                }
            }
        }));

        return transform(evaluated, Suppliers.<Arg>supplierFunction());
    }

    private class ThrowingSupplier<Arg> implements Supplier<Arg> {
        private final RuntimeException e;

        public ThrowingSupplier(RuntimeException e) {
            this.e = e;
        }

        @Override
        public Arg get() {
            throw e;
        }
    }
}
