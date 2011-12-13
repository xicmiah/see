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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.MutableClassToInstanceMap;
import see.evaluation.Evaluator;
import see.evaluation.ValueProcessor;
import see.evaluation.visitors.LazyVisitor;
import see.exceptions.EvaluationException;
import see.functions.Function;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.reactive.impl.ReactiveFactory;
import see.tree.Node;

import java.util.List;
import java.util.Map;

public class ReactiveEvaluator implements Evaluator {

    private final GrammarConfiguration config;
    private final ReactiveFactory reactiveFactory;
    private final ValueProcessor processor;

    public ReactiveEvaluator(GrammarConfiguration config, ReactiveFactory reactiveFactory, ValueProcessor processor) {
        this.config = config;
        this.reactiveFactory = reactiveFactory;
        this.processor = processor;
    }

    @Override
    public <T> T evaluate(Node<T> tree, Map<String, ?> initial) throws EvaluationException {
        Map<String, Object> mutable = Maps.newHashMap(initial);
        Map<String, ?> constants = ImmutableMap.of();

        ClassToInstanceMap<Object> services = MutableClassToInstanceMap.create();
        services.putInstance(NumberFactory.class, config.getNumberFactory());
        services.putInstance(ChainResolver.class, config.getChainResolver());
        services.putInstance(ReactiveFactory.class, reactiveFactory);
        services.putInstance(ValueProcessor.class, processor);

        SimpleContext context = new SimpleContext(mutable, constants, services);

        Map<String, Function<List<Object>, Object>> boundFunctions = config.getFunctions().getBoundFunctions(context);
        for (Map.Entry<String, Function<List<Object>, Object>> entry : boundFunctions.entrySet()) {
            context.addConstant(entry.getKey(), entry.getValue());
        }

        return tree.accept(new LazyVisitor(context, processor, config.getChainResolver()));
    }
}
