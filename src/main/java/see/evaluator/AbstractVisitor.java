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

package see.evaluator;

import com.google.common.base.Function;
import see.tree.*;
import see.util.Reduce;

import java.util.List;
import java.util.Map;

import static see.util.Reduce.fold;

public abstract class AbstractVisitor implements Visitor {
    private final Map<String, ?> context;
    private List<ValueProcessor> valueProcessors;

    public AbstractVisitor(Map<String, ?> context, List<ValueProcessor> valueProcessors) {
        this.context = context;
        this.valueProcessors = valueProcessors;
    }

    @Override
    public <Arg, Result> Result visit(FunctionNode<Arg, Result> node) {
        List<Arg> evaluatedArgs = evaluateArgs(node.getArguments());

		// Note: evaluatedArgs are lazy
        see.functions.Function<List<Arg>, Result> partial = node.getFunction().apply(context);
        Result result = partial.apply(evaluatedArgs);

        return processValue(result);
	}

    protected abstract <Arg> List<Arg> evaluateArgs(List<Node<Arg>> arguments);

    /**
     * Get variable value from context.
     * If context holds a Number, the value is passed through NumberFactory,
     * otherwise it's returned directly.
     *
     * @param node visited node
     * @param <T> node type
     * @return extracted value
     */
	@Override
    @SuppressWarnings("unchecked")
    public <T> T visit(VarNode<T> node) {
        Object value = context.get(node.getName());

        return (T) processValue(value);
	}

    @Override
    public <T> T visit(ConstNode<T> node) {
		return node.getValue();
	}

    /**
     * Pass value through post-processors.
     * @param value
     * @param <T>
     * @return
     */
    private <T> T processValue(T value) {
        return (T) fold(value, valueProcessors, new Reduce.FoldFunction<Function<Object, Object>, Object>() {
            @Override
            public Object apply(Object prev, Function<Object, Object> arg) {
                return arg.apply(prev);
            }
        });
    }
}
