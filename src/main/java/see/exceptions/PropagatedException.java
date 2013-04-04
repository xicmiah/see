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

package see.exceptions;

import see.tree.Node;

/**
 * Evaluation-time error, contains tree node, where evaluation failed.
 */
public class PropagatedException extends EvaluationException {
    private final Node<?> failedNode;

    private final Throwable lastCause;

    public PropagatedException(Node<?> failedNode, Throwable cause) {
        super(formatMessage(failedNode), cause);
        this.failedNode = failedNode;
        this.lastCause = getLastCause(cause);
    }

    private static String formatMessage(Node<?> failedNode) {
        return "Evaluation failed at node " + failedNode;
    }

    private static Throwable getLastCause(Throwable cause) {
        return cause instanceof PropagatedException ? ((PropagatedException) cause).getLastCause() : cause;
    }

    public Node<?> getFailedNode() {
        return failedNode;
    }

    public Throwable getLastCause() {
        return lastCause;
    }
}
