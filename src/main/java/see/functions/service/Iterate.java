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
import see.evaluation.Context;
import see.evaluation.ContextEvaluator;
import see.evaluation.Scope;
import see.functions.ContextCurriedFunction;
import see.functions.VarArgFunction;
import see.tree.Node;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static see.evaluation.evaluators.SimpleContext.withVariables;
import static see.evaluation.scopes.Scopes.override;


/**
 * Iterate. Executes a function for every item in sequence.
 *
 * Takes three arguments: string variable name, Iterable and a function.
 * For each item in sequence it puts item into context under specified name and executes function.
 * Returns result of the last function.
 *
 * This implementation assumes that evaluator doesn't cache evaluation results,
 * i.e. each time get(int) is called on arguments, corresponding sub-tree is evaluated.
 */
public class Iterate implements ContextCurriedFunction<Object, Object> {
    @Override
    public VarArgFunction<Object, Object> apply(@Nonnull final Context context) {
        return new VarArgFunction<Object, Object>() {
            @Override
            public Object apply(@Nonnull List<Object> input) {
                Preconditions.checkArgument(input.size() == 3, "Iterate takes 3 arguments");

                String varName = (String) input.get(0);
                Iterable<?> list = (Iterable<?>) input.get(1);
                Node<?> tree = (Node<?>) input.get(2);

                ContextEvaluator evaluator = context.getServices().getInstance(ContextEvaluator.class);

                Object lastValue = null;
                for (Object item : list) {
                    Scope scope = override(context.getScope(), of(varName, item));
                    lastValue = evaluator.evaluate(tree, withVariables(context, scope));
                }
                
                return lastValue;
            }
        };
    }

    @Override
    public String toString() {
        return "iterate";
    }
}
