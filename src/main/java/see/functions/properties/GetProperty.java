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
import see.util.Reduce;

import java.math.BigDecimal;
import java.util.List;

import static see.util.Reduce.fold;

public class GetProperty implements VarArgFunction<PropertyAccess, Object> {
    @Override
    public Object apply(List<PropertyAccess> input) {
        Preconditions.checkArgument(input.size() >= 1, "GetProperty takes one or more arguments");

        final GetVisitor getVisitor = new GetVisitor();

        return fold(null, input, new Reduce.FoldFunction<PropertyAccess, Object>() {
            @Override
            public Object apply(Object prev, PropertyAccess arg) {
                return arg.accept(getVisitor, prev);
            }
        });
    }

    private static class GetVisitor implements PropertyAccess.Visitor<Object, Object> {
        @Override
        public Object visit(PropertyAccess.Target target, Object prev) {
            return target.getTarget();
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
                    return PropertyUtils.getIndexedProperty(target, "", parseInt((Number) index));
                } else if (index instanceof String) {
                    return PropertyUtils.getSimpleProperty(target, (String) index);
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
        return "get";
    }
}
