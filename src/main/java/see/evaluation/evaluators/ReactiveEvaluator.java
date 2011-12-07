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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import see.evaluation.Evaluator;
import see.evaluation.ValueProcessor;
import see.evaluation.processors.NumberLifter;
import see.evaluation.visitors.LazyVisitor;
import see.exceptions.EvaluationException;
import see.parser.config.GrammarConfiguration;
import see.reactive.impl.ReactiveFactory;
import see.tree.Node;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Suppliers.ofInstance;

public class ReactiveEvaluator implements Evaluator {
    public static final String CONFIG_KEY = "⑨config";
    public static final String PROCESSORS_KEY = "⑨processors";
    public static final String REACTIVE_KEY = "⑨reactive";

    private final GrammarConfiguration config;
    private final ReactiveFactory reactiveFactory;
    private final List<ValueProcessor> customProcessors;

    public ReactiveEvaluator(GrammarConfiguration config, ReactiveFactory reactiveFactory, List<ValueProcessor> processors) {
        this.config = config;
        this.reactiveFactory = reactiveFactory;
        this.customProcessors = processors;
    }

    @Override
    public <T> T evaluate(Node<T> tree, Map<String, ?> context) throws EvaluationException {
        List<ValueProcessor> processors = ImmutableList.<ValueProcessor>builder()
                .addAll(customProcessors)
                .add(new NumberLifter(ofInstance(config.getNumberFactory())))
                .build();

        final Map<String, Object> reactiveContext = Maps.newHashMap(context);
        reactiveContext.put(CONFIG_KEY, config);
        reactiveContext.put(REACTIVE_KEY, reactiveFactory);
        reactiveContext.put(PROCESSORS_KEY, processors);

        reactiveContext.putAll(config.getFunctions().getBoundFunctions(reactiveContext));
        
        return tree.accept(new LazyVisitor(reactiveContext, processors, config.getChainResolver()));
    }
}
