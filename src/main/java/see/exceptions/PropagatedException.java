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

    private PropagatedException(Node<?> failedNode, Throwable cause, Throwable lastCause) {
        super(formatMessage(failedNode), cause);
        this.failedNode = failedNode;
        this.lastCause = lastCause;
    }

    public PropagatedException(Node<?> failedNode, Throwable cause) {
        this(failedNode, cause, cause);
    }

    public PropagatedException(Node<?> failedNode, PropagatedException cause) {
        this(failedNode, cause, cause.getLastCause());
    }

    private static String formatMessage(Node<?> failedNode) {
        return new StringBuilder("Evaluation failed at node ").append(failedNode).toString();
    }

    public Node<?> getFailedNode() {
        return failedNode;
    }

    public Throwable getLastCause() {
        return lastCause;
    }
}
