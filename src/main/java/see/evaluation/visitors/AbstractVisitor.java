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
import see.evaluation.Context;
import see.evaluation.ValueProcessor;
import see.exceptions.PropagatedException;
import see.functions.Property;
import see.parser.grammar.PropertyAccess;
import see.parser.grammar.PropertyDescriptor;
import see.properties.ChainResolver;
import see.tree.*;

import java.util.List;

import static com.google.common.collect.Lists.transform;

public abstract class AbstractVisitor implements Visitor {
    private final Context context;
    private final ValueProcessor valueProcessor;
    private final ChainResolver resolver;


    public AbstractVisitor(Context context, ValueProcessor valueProcessor, ChainResolver resolver) {
        this.context = context;
        this.valueProcessor = valueProcessor;
        this.resolver = resolver;
    }

    @Override
    public <Arg, Result> Result visit(FunctionNode<Arg, Result> node) {
        try {
            List<Arg> evaluatedArgs = evaluateArgs(node.getArguments());

            // Note: evaluatedArgs are lazy
            see.functions.Function<List<Arg>, Result> partial = node.getFunction().apply(context);
            Result result = partial.apply(evaluatedArgs);

            return processValue(result);
        } catch (PropagatedException e) {
            throw new PropagatedException(node, e);
        }  catch (Exception e) {
            throw new PropagatedException(node, e);
        }
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T visit(PropertyNode<T> propertyNode) {
        try {
            final Object target = propertyNode.getTarget().accept(this);

            final List<? extends PropertyAccess> evaluatedProps = evaluateProperties(propertyNode.getProperties());

            return (T) new Property<Object>() {
                @Override
                public void set(Object value) {
                    resolver.set(target, evaluatedProps, value);
                }

                @Override
                public Object get() {
                    return processValue(resolver.get(target, evaluatedProps));
                }
            };
        } catch (PropagatedException e) {
            throw new PropagatedException(propertyNode, e);
        } catch (Exception e) {
            throw new PropagatedException(propertyNode, e);
        }
    }

    private List<? extends PropertyAccess> evaluateProperties(List<? extends PropertyDescriptor> initialProps) {
        return transform(initialProps, new Function<PropertyDescriptor, PropertyAccess>() {
            @Override
            public PropertyAccess apply(PropertyDescriptor input) {
                return evaluateProperty(input);
            }
        });
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private PropertyAccess evaluateProperty(PropertyDescriptor input) {
        for (String name : input.value().left()) {
            return PropertyAccess.simple(name);
        }
        for (Node<?> index : input.value().right()) {
            return PropertyAccess.indexed(index.accept(this));
        }

        throw new IllegalStateException("Either has no value, will never happen");
    }

    /**
     * Pass value through post-processors.
     * @param value initial value
     * @param <T> value type
     * @return processed value
     */
    @SuppressWarnings("unchecked")
    private <T> T processValue(T value) {
        return (T) valueProcessor.apply(value);
    }
}
