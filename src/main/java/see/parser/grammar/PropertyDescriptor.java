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

package see.parser.grammar;

import see.tree.Node;
import see.util.Either;

/**
 * Parse-time property description.
 * Property can be either simple("a.prop") described by string name,
 * or indexed ("a[prop]") described by arbitrary expression (corresponding tree here).
 */
public abstract class PropertyDescriptor {
    private PropertyDescriptor() {
    }

    /**
     * Get corresponding property value (string for simple, tree for indexed)
     * @return either string or tree
     */
    public abstract Either<String, Node<Object>> value();

    public static Simple simple(String name) {
        return new Simple(name);
    }

    public static Indexed indexed(Node<Object> index) {
        return new Indexed(index);
    }

    public static class Simple extends PropertyDescriptor {
        private final String name;

        private Simple(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public Either<String, Node<Object>> value() {
            return Either.left(name);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(".").append(name);
            return sb.toString();
        }
    }

    public static class Indexed extends PropertyDescriptor {
        private final Node<Object> index;

        private Indexed(Node<Object> index) {
            this.index = index;
        }

        public Node<?> getIndex() {
            return index;
        }

        @Override
        public Either<String, Node<Object>> value() {
            return Either.right(index);
        }

        @Override
        public String toString() {
            return new StringBuilder("[").append(index).append("]").toString();
        }
    }
}
