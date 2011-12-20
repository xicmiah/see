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

import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import see.evaluation.Evaluator;
import see.evaluation.ToFunction;
import see.evaluation.ValueProcessor;
import see.evaluation.conversions.VarArgIdentity;
import see.evaluation.visitors.LazyVisitor;
import see.exceptions.EvaluationException;
import see.functions.Function;
import see.parser.config.FunctionResolver;
import see.parser.config.GrammarConfiguration;
import see.parser.numbers.NumberFactory;
import see.properties.ChainResolver;
import see.reactive.impl.ReactiveFactory;
import see.tree.Node;

import java.util.List;
import java.util.Map;

public class ReactiveEvaluator implements Evaluator {

    private final FunctionResolver functionResolver;
    
    private final NumberFactory numberFactory;
    private final ChainResolver resolver;
    private final ValueProcessor valueProcessor;

    private final ReactiveFactory reactiveFactory;

    public ReactiveEvaluator(GrammarConfiguration config, ReactiveFactory reactiveFactory) {
        this.functionResolver = config.getFunctions();

        this.numberFactory = config.getNumberFactory();
        this.resolver = config.getChainResolver();
        this.valueProcessor = config.getValueProcessor();

        this.reactiveFactory = reactiveFactory;
    }

    @Override
    public <T> T evaluate(Node<T> tree, Map<String, ?> initial) throws EvaluationException {
        Map<String, Object> mutable = Maps.newHashMap(initial);
        Map<String, ?> constants = ImmutableMap.of();

        SimpleContext context = new SimpleContext(mutable, constants, createServices());

        Map<String, Function<List<Object>, Object>> boundFunctions = functionResolver.getBoundFunctions(context);
        for (Map.Entry<String, Function<List<Object>, Object>> entry : boundFunctions.entrySet()) {
            context.addConstant(entry.getKey(), entry.getValue());
        }

        return tree.accept(new LazyVisitor(context, valueProcessor, resolver));
    }

    private ImmutableClassToInstanceMap<Object> createServices() {
        return ImmutableClassToInstanceMap.builder()
                .put(NumberFactory.class, numberFactory)
                .put(ChainResolver.class, resolver)
                .put(ValueProcessor.class, valueProcessor)
                .put(ReactiveFactory.class, reactiveFactory)
                .put(ToFunction.class, new VarArgIdentity())
                .put(Evaluator.class, this)
                .build();
    }
}
