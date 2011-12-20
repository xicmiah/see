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

package see.functions.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import see.evaluation.Context;
import see.evaluation.Evaluator;
import see.functions.ContextCurriedFunction;
import see.functions.VarArgFunction;
import see.tree.Node;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Function creation. Takes list of argument names and tree, returns function,
 * which takes same number of arguments and evaluates tree against context with these arguments.
 */
public class MakeFunction implements ContextCurriedFunction<VarArgFunction<Object, VarArgFunction<Object, Object>>> {
    @Override
    public VarArgFunction<Object, VarArgFunction<Object, Object>> apply(@Nonnull final Context context) {
        return new ContextClosure(context);
    }

    @Override
    public String toString() {
        return "def";
    }

    private static class ContextClosure implements VarArgFunction<Object, VarArgFunction<Object, Object>> {
        private final Context context;
        private final Evaluator evaluator;

        public ContextClosure(Context context) {
            this.context = context;
            this.evaluator = getEvaluator();
        }

        @Override
        public VarArgFunction<Object, Object> apply(@Nonnull List<Object> input) {
            final List<String> argNames = (List<String>) input.get(0);
            final Node<?> tree = (Node<?>) input.get(1);

            return new CreatedFunction(argNames, tree);
        }


        private Evaluator getEvaluator() {
            return context.getServices().getInstance(Evaluator.class);
        }

        private class CreatedFunction implements VarArgFunction<Object, Object> {
            private final List<String> argNames;
            private final Node<?> tree;

            public CreatedFunction(List<String> argNames, Node<?> tree) {
                this.argNames = argNames;
                this.tree = tree;
            }

            @Override
            public Object apply(@Nonnull List<Object> actualArgs) {
                Preconditions.checkArgument(actualArgs.size() == argNames.size(), "Wrong number of arguments");

                return evaluator.evaluate(tree, createPatchedContext(actualArgs));
            }

            private Map<String, ?> createPatchedContext(List<Object> args) {
                Map<String,Object> patchedContext = Maps.newHashMap(context.asMap());

                Iterator<Object> objects = args.iterator();
                for (String argName : argNames) {
                    patchedContext.put(argName, objects.next());
                }

                return patchedContext;
            }
        }
    }
}
