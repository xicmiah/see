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

package see.functions.properties;

import com.google.common.base.Preconditions;
import org.apache.commons.beanutils.PropertyUtils;
import see.exceptions.EvaluationException;
import see.functions.VarArgFunction;
import see.parser.grammar.PropertyAccess;

import java.math.BigDecimal;
import java.util.List;

public class SetProperty implements VarArgFunction<PropertyAccess, Object> {
    @Override
    public Object apply(List<PropertyAccess> input) {
        Preconditions.checkArgument(input.size() >= 3, "SetProperty takes three or more arguments");

        List<PropertyAccess> beanPath = last(input);
        Object value = getValue(input);

        Object bean = new GetProperty().apply(last(beanPath));
        PropertyAccess lastProperty = getLast(beanPath);

        lastProperty.accept(new SetVisitor(value), bean);

        return value;
    }

    private <T> T getLast(List<T> beanPath) {
        return beanPath.get(beanPath.size() - 1);
    }

    private List<PropertyAccess> last(List<PropertyAccess> input) {
        return input.subList(0, input.size() - 1);
    }

    private Object getValue(List<PropertyAccess> input) {
        PropertyAccess.Target toAssign = (PropertyAccess.Target) getLast(input);
        return toAssign.getTarget();
    }

    public static class SetVisitor implements PropertyAccess.Visitor<Object, Object> {
        private final Object value;

        public SetVisitor(Object value) {
            this.value = value;
        }

        @Override
        public Object visit(PropertyAccess.Target target, Object prev) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object visit(PropertyAccess.Simple simple, Object target) {
            String property = simple.getName();
            try {
                PropertyUtils.setSimpleProperty(target, property, value);
            } catch (Exception e) {
                throw new EvaluationException("Couldn't set property " + property, e);
            }

            return value;
        }

        @Override
        public Object visit(PropertyAccess.Indexed indexed, Object target) {
            Object index = indexed.getIndex();
            try {
                if (index instanceof Number) {
                    PropertyUtils.setIndexedProperty(target, "", parseInt((Number) index), value);
                    return value;
                } else if (index instanceof String) {
                    PropertyUtils.setSimpleProperty(target, (String) index, value);
                    return value;
                }
            } catch (Exception e) {
                throw new EvaluationException("Couldn't read indexed property " + index, e);
            }

            throw new IllegalArgumentException("Bad indexed property " + index);
        }

        private int parseInt(Number index) {
            if (index instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) index;
                return bigDecimal.intValueExact();
            }

            return index.intValue();
        }

    }

    @Override
    public String toString() {
        return "set";
    }
}
