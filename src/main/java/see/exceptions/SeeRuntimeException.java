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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import scala.Option;
import see.tree.Node;
import see.tree.trace.TraceElement;
import see.tree.trace.Tracing;

import java.util.List;

public class SeeRuntimeException extends EvaluationException {
    private final List<TraceElement> trace;

    public SeeRuntimeException(Node<?> node, Throwable cause) {
        super(getMessage(node, cause), getProperCause(cause));
        this.trace = buildTrace(node, cause);
    }

    private static List<TraceElement> buildTrace(Node<?> node, Throwable cause) {
        ImmutableList.Builder<TraceElement> builder = ImmutableList.builder();
        builder.addAll(getTrace(node));

        if (cause instanceof SeeRuntimeException) {
            builder.addAll(((SeeRuntimeException) cause).getTrace());
        }
        return builder.build();
    }

    private static String getMessage(Node<?> node, Throwable cause) {
        if (cause instanceof SeeRuntimeException) {
            return formatMessage(buildTrace(node, cause));
        } else {
            return formatMessage(getTrace(node));
        }
    }

    private static Throwable getProperCause(Throwable cause) {
        if (cause instanceof SeeRuntimeException) {
            return cause.getCause();
        } else {
            return cause;
        }
    }

    private static Iterable<TraceElement> getTrace(Node<?> node) {
        Option<TraceElement> position = ((Tracing) node).position();
        if (position.isDefined()) {
            return ImmutableList.of(position.get());
        } else {
            return ImmutableList.of();
        }
    }

    public List<TraceElement> getTrace() {
        return trace;
    }

    private static String formatMessage(Iterable<TraceElement> trace) {
        return "\n" + Joiner.on("").join(trace);
    }
}
