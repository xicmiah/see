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

public abstract class PropertyAccess {
    private PropertyAccess() {}

    public abstract <T> T accept(Visitor<T> visitor);

    public static class Target extends PropertyAccess {
        private final Object target;

        public Target(Object target) {
            this.target = target;
        }

        public Object getTarget() {
            return target;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.accept(this);
        }
    }

    public static class Simple extends PropertyAccess {
        private final String name;

        public Simple(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.accept(this);
        }
    }

    public static class Indexed extends PropertyAccess {
        private final Object index;

        public Indexed(Object index) {
            this.index = index;
        }

        public Object getIndex() {
            return index;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.accept(this);
        }
    }

    public static interface Visitor<T> {
        T accept(Target target);

        T accept(Simple simple);

        T accept(Indexed indexed);
    }
}
