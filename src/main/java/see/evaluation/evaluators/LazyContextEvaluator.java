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

package see.evaluation.evaluators;

import com.google.common.collect.ClassToInstanceMap;
import see.evaluation.Context;
import see.evaluation.ContextEvaluator;
import see.evaluation.ValueProcessor;
import see.evaluation.visitors.LazyVisitor;
import see.properties.ChainResolver;
import see.tree.Node;

import static see.evaluation.evaluators.SimpleContext.addService;

public class LazyContextEvaluator implements ContextEvaluator {
    @Override
    public <T> T evaluate(Node<T> tree, Context context) {
        ClassToInstanceMap<Object> services = context.getServices();
        ValueProcessor valueProcessor = services.getInstance(ValueProcessor.class);
        ChainResolver chainResolver = services.getInstance(ChainResolver.class);

        Context actualContext = addService(context, ContextEvaluator.class, this);

        return tree.accept(new LazyVisitor(actualContext, valueProcessor, chainResolver));
    }
}
