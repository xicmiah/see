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

import org.apache.commons.beanutils.PropertyUtils;
import see.exceptions.EvaluationException;
import see.parser.grammar.PropertyAccess;
import see.util.Reduce;

import java.util.List;

import static see.util.Reduce.fold;

public class PropertyUtilsResolver implements PropertyResolver {
    @Override
    public Object get(Object bean, List<? extends PropertyAccess> properties) {
        final GetVisitor getVisitor = new GetVisitor();

        return fold(bean, properties, new Reduce.FoldFunction<PropertyAccess, Object>() {
            @Override
            public Object apply(Object prev, PropertyAccess arg) {
                return arg.accept(getVisitor, prev);
            }
        });
    }

    @Override
    public void set(Object bean, List<? extends PropertyAccess> properties, Object value) {
        Object lastBean = get(bean, properties.subList(0, properties.size() - 1));
        PropertyAccess lastProperty = properties.get(properties.size() - 1);

        lastProperty.accept(new SetVisitor(value), lastBean);
    }

    private static int parseIndex(Number index) {
        return index.intValue();
    }



    private static class GetVisitor implements PropertyAccess.Visitor<Object, Object> {
        @Override
        public Object visit(PropertyAccess.Value value, Object prev) {
            return value.getTarget();
        }

        @Override
        public Object visit(PropertyAccess.Simple simple, Object target) {
            String property = simple.getName();
            try {
                return PropertyUtils.getProperty(target, property);
            } catch (Exception e) {
                throw new EvaluationException("Couldn't read simple property " + property, e);
            }
        }

        @Override
        public Object visit(PropertyAccess.Indexed indexed, Object target) {
            Object index = indexed.getIndex();
            try {
                if (index instanceof Number) {
                    return PropertyUtils.getIndexedProperty(target, "", parseIndex((Number) index));
                } else if (index instanceof String) {
                    return PropertyUtils.getSimpleProperty(target, (String) index);
                }
            } catch (Exception e) {
                throw new EvaluationException("Couldn't read indexed property " + index, e);
            }

            throw new IllegalArgumentException("Bad indexed property " + index);
        }

    }

    private static class SetVisitor implements PropertyAccess.Visitor<Object, Object> {
        private final Object value;

        public SetVisitor(Object value) {
            this.value = value;
        }

        @Override
        public Object visit(PropertyAccess.Value value, Object prev) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object visit(PropertyAccess.Simple simple, Object target) {
            String property = simple.getName();
            try {
                PropertyUtils.setSimpleProperty(target, property, value);
            } catch (Exception e) {
                throw new EvaluationException("Couldn't set simple property " + property, e);
            }

            return value;
        }

        @Override
        public Object visit(PropertyAccess.Indexed indexed, Object target) {
            Object index = indexed.getIndex();
            try {
                if (index instanceof Number) {
                    PropertyUtils.setIndexedProperty(target, "", parseIndex((Number) index), value);
                    return value;
                } else if (index instanceof String) {
                    PropertyUtils.setSimpleProperty(target, (String) index, value);
                    return value;
                }
            } catch (Exception e) {
                throw new EvaluationException("Couldn't write indexed property " + index, e);
            }

            throw new IllegalArgumentException("Bad indexed property " + index);
        }
    }

}
