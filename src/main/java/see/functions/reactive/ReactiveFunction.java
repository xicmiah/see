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

package see.functions.reactive;

import see.evaluator.ReactiveEvaluator;
import see.evaluator.ValueProcessor;
import see.functions.ContextCurriedFunction;
import see.functions.Function;
import see.functions.VarArgFunction;
import see.parser.config.GrammarConfiguration;
import see.reactive.impl.ReactiveFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public abstract class ReactiveFunction<Arg, Result> implements ContextCurriedFunction<Function<List<Arg>, Result>> {


    @Override
    public Function<List<Arg>, Result> apply(final Map<String, ?> context) {
        ReactiveFactory factory = (ReactiveFactory) context.get(ReactiveEvaluator.REACTIVE_KEY);
        GrammarConfiguration config = (GrammarConfiguration) context.get(ReactiveEvaluator.CONFIG_KEY);
        List<ValueProcessor> processors = (List<ValueProcessor>) context.get(ReactiveEvaluator.PROCESSORS_KEY);

        final ContextConfig contextConfig = new ContextConfig(factory, config, processors);

        return new VarArgFunction<Arg, Result>() {
            @Override
            public Result apply(@Nonnull List<Arg> input) {
                return ReactiveFunction.this.apply(contextConfig, input, context);
            }
        };
    }

    protected abstract Result apply(ContextConfig config, List<Arg> input, Map<String, ?> context);

    protected static class ContextConfig {
        private final ReactiveFactory reactiveFactory;
        private final GrammarConfiguration grammarConfig;
        private final List<ValueProcessor> valueProcessors;

        public ContextConfig(ReactiveFactory reactiveFactory, GrammarConfiguration grammarConfig, List<ValueProcessor> valueProcessors) {
            this.reactiveFactory = reactiveFactory;
            this.grammarConfig = grammarConfig;
            this.valueProcessors = valueProcessors;
        }

        public ReactiveFactory getReactiveFactory() {
            return reactiveFactory;
        }

        public GrammarConfiguration getGrammarConfig() {
            return grammarConfig;
        }

        public List<ValueProcessor> getValueProcessors() {
            return valueProcessors;
        }
    }
}
