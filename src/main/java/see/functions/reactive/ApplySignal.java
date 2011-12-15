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

import com.google.common.base.Preconditions;
import see.functions.service.Apply;
import see.reactive.Signal;
import see.reactive.Sink;

import javax.annotation.Nonnull;
import java.util.List;

public class ApplySignal extends Apply {

    private final Sink<? super Signal<?>> sink;

    public ApplySignal(Sink<? super Signal<?>> sink) {
        this.sink = sink;
    }

    @Override
    public Object apply(@Nonnull List<Object> input) {
        Preconditions.checkArgument(input.size() >= 1, "Apply takes one or more arguments");
        Object target = input.get(0);

        if (target instanceof Signal<?> && input.size() == 1) {
            Signal<?> signal = (Signal<?>) target;
            sink.accept(signal);

            return signal.now();
        } else {
            return super.apply(input);
        }
    }

    @Override
    public String toString() {
        return "applyS";
    }
}
