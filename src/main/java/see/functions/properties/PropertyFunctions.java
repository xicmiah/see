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
import see.functions.VarArgFunction;
import see.parser.grammar.PropertyAccess;
import see.util.Reduce;

import java.util.List;

import static see.util.Reduce.fold;

public class PropertyFunctions {
    private final PropertyResolver resolver;

    private final Get getInstance = new Get();
    private final Set setInstance = new Set();

    public PropertyFunctions(PropertyResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Return instance of getter function
     * @return configured Get instance
     */
    public Get getGetFunction() {
        return getInstance;
    }

    /**
     * Return instance of setter function
     * @return configured Set instance
     */
    public Set getSetFunction() {
        return setInstance;
    }

    private Object getBean(PropertyAccess input) {
        Preconditions.checkArgument(input instanceof PropertyAccess.Value, "Cannot get value from " + input);

        PropertyAccess.Value value = (PropertyAccess.Value) input;
        return value.getTarget();
    }

    public class Get implements VarArgFunction<PropertyAccess, Object> {
        private Get() {}

        @Override
        public Object apply(List<PropertyAccess> input) {
            Preconditions.checkArgument(input.size() >= 1, "GetProperty takes one or more arguments");

            Object bean = getBean(input.get(0));
            List<PropertyAccess> properties = input.subList(1, input.size());

            return fold(bean, properties, new Reduce.FoldFunction<PropertyAccess, Object>() {
                @Override
                public Object apply(Object prev, PropertyAccess arg) {
                    return resolver.get(prev, arg);
                }
            });
        }
        @Override
        public String toString() {
            return "get";
        }

    }

    public class Set implements VarArgFunction<PropertyAccess, Object> {
        private Set() {}

        @Override
        public Object apply(List<PropertyAccess> input) {
            Preconditions.checkArgument(input.size() >= 3, "SetProperty takes three or more arguments");

            List<PropertyAccess> properties = input.subList(0, input.size() - 1);
            Object value = getBean(input.get(input.size() - 1));

            Object lastBean = getInstance.apply(properties.subList(0, properties.size() - 1));
            PropertyAccess lastProperty = properties.get(properties.size() - 1);

            resolver.set(lastBean, lastProperty, value);
            
            return value;
        }

        @Override
        public String toString() {
            return "set";
        }
    }
}
