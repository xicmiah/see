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

package see.evaluation.processors;

import see.evaluation.ValueProcessor;
import see.util.Reduce;

import javax.annotation.Nullable;

import static java.util.Arrays.asList;
import static see.util.Reduce.fold;

public class AggregatingProcessor implements ValueProcessor {
    private final Iterable<? extends ValueProcessor> processors;

    private AggregatingProcessor(Iterable<? extends ValueProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public Object apply(@Nullable Object input) {
        return fold(input, processors, new Reduce.FoldFunction<ValueProcessor, Object>() {
            @Override
            public Object apply(Object prev, ValueProcessor arg) {
                return arg.apply(prev);
            }
        });
    }

    public static ValueProcessor concat(ValueProcessor... processors) {
        return new AggregatingProcessor(asList(processors));
    }

    public static ValueProcessor concat(Iterable<? extends ValueProcessor> processors) {
        return new AggregatingProcessor(processors);
    }
}
