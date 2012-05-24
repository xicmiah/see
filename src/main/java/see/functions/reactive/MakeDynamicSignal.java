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

package see.functions.reactive;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import see.evaluation.Context;
import see.evaluation.ToFunction;
import see.evaluation.ValueProcessor;
import see.evaluation.evaluators.SimpleContext;
import see.evaluation.visitors.LazyVisitor;
import see.functions.ContextCurriedFunction;
import see.functions.VarArgFunction;
import see.properties.ChainResolver;
import see.reactive.EvaluationResult;
import see.reactive.Signal;
import see.reactive.SignalFactory;
import see.tree.Node;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

import static see.evaluation.conversions.FunctionConversions.concat;

/**
 * Signal creation. Expects second argument to be a tree.
 * Creates a signal bound to signals present in tree.
 */
public class MakeDynamicSignal implements ContextCurriedFunction<Object, Signal<?>> {
    @Override
    public VarArgFunction<Object, Signal<?>> apply(@Nonnull final Context context) {
        return new VarArgFunction<Object, Signal<?>>() {
            @Override
            public Signal<?> apply(@Nonnull List<Object> input) {
                Preconditions.checkArgument(input.size() == 2, "MakeSignal takes two arguments");

                final Node<Object> tree = (Node<Object>) input.get(1);

                SignalFactory signalFactory = context.getServices().getInstance(SignalFactory.class);
                return signalFactory.bindDynamic(new Supplier<EvaluationResult<Object>>() {
                    @Override
                    public EvaluationResult<Object> get() {
                        Collection<Signal<?>> dependencies = Sets.newHashSet();

                        Context extractionContext = getPatchedContext(new SignalExtractor(dependencies));
                        Object result = tree.accept(new LazyVisitor(extractionContext, getProcessor(), getResolver()));

                        return new EvaluationResult<Object>(result, dependencies);
                    }
                });
            }

            @Override
            public String toString() {
                return "signal";
            }

            private Context getPatchedContext(ToFunction signalFunction) {
                ToFunction old = context.getServices().getInstance(ToFunction.class);

                return SimpleContext.addService(context, ToFunction.class, concat(signalFunction, old));
            }

            private ValueProcessor getProcessor() {
                return context.getServices().getInstance(ValueProcessor.class);
            }

            private ChainResolver getResolver() {
                return context.getServices().getInstance(ChainResolver.class);
            }
        };
    }

    @Override
    public String toString() {
        return "signal";
    }

    private static class SignalExtractor implements ToFunction {
        private final Collection<? super Signal<?>> dependencies;

        private SignalExtractor(Collection<? super Signal<?>> dependencies) {
            this.dependencies = dependencies;
        }

        @Nonnull
        @Override
        public ContextCurriedFunction<Object, ?> apply(@Nonnull Object input) {
            final Signal<?> signal = (Signal<?>) input;
            return new ContextCurriedFunction<Object, Object>() {
                @Override
                public VarArgFunction<Object, Object> apply(@Nonnull Context context) {
                    return new VarArgFunction<Object, Object>() {
                        @Override
                        public Object apply(@Nonnull List<Object> objects) {
                            return signal.now();
                        }
                    };
                }
            };
        }

        @Override
        public boolean isDefinedAt(Object input) {
            if (input instanceof Signal<?>) {
                dependencies.add((Signal<?>) input);
            }

            return input instanceof Signal<?>;
        }
    }

}
