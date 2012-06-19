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

package see.tree.immutable;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import see.parser.grammar.PropertyDescriptor;
import see.tree.Node;
import see.tree.PropertyNode;
import see.tree.ValueVisitor;
import see.tree.Visitor;

import java.util.List;

public class ImmutablePropertyNode<T> implements PropertyNode<T> {
    
    private final Node<?> target;
    private final List<? extends PropertyDescriptor> properties;

    public static <T> PropertyNode<T> propertyNode(Node<?> target, List<? extends PropertyDescriptor> properties) {
        return new ImmutablePropertyNode<T>(target, properties);
    }

    private ImmutablePropertyNode(Node<?> target, List<? extends PropertyDescriptor> properties) {
        this.target = target;
        this.properties = properties;
    }

    @Override
    public Node<?> getTarget() {
        return target;
    }

    @Override
    public List<? extends PropertyDescriptor> getProperties() {
        return properties;
    }

    @Override
    public T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public <V> V accept(ValueVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutablePropertyNode that = (ImmutablePropertyNode) o;

        return Objects.equal(properties, that.properties) && Objects.equal(target, that.target);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target, properties);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Prop(");
        sb.append(target).append(Joiner.on("").join(properties));
        sb.append(')');
        return sb.toString();
    }
}
