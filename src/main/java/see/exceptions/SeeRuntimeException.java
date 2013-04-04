/*
 * Copyright 2012 Vasily Shiyan
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

package see.exceptions;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import scala.Option;
import see.tree.Node;
import see.tree.trace.TraceElement;
import see.tree.trace.Tracing;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.base.Throwables.getCausalChain;
import static com.google.common.collect.FluentIterable.from;

public class SeeRuntimeException extends EvaluationException {
    private final List<TraceElement> trace;

    /**
     * Extract see stacktrace from a throwable
     * @param throwable target throwable
     * @return extracted trace
     */
    public static List<TraceElement> getTrace(Throwable throwable) {
        List<Throwable> causalChain = getCausalChain(throwable);
        FluentIterable<PropagatedException> stack = from(causalChain).filter(PropagatedException.class);
        FluentIterable<Node<?>> nodes = stack.transform(new Function<PropagatedException, Node<?>>() {
            @Override
            public Node<?> apply(PropagatedException input) {
                return input.getFailedNode();
            }
        });
        FluentIterable<TraceElement> trace = nodes.filter(Tracing.class)
                .transform(new Function<Tracing, TraceElement>() {
                    @Nullable
                    @Override
                    public TraceElement apply(Tracing input) {
                        Option<TraceElement> position = input.position();
                        if (position.isDefined()) return position.get();
                        else return null;
                    }
                }).filter(notNull());
        return trace.toList();
    }

    public SeeRuntimeException(List<TraceElement> trace, Throwable cause) {
        super("\n" + Joiner.on("").join(trace), cause);
        this.trace = trace;
    }

    public List<TraceElement> getTrace() {
        return trace;
    }
}
