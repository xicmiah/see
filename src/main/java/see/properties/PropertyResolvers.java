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

package see.properties;

import see.parser.grammar.PropertyAccess;


public class PropertyResolvers {
    private PropertyResolvers() {}

    /**
     * Get a partial resolver, which resolves everything
     * @param resolver wrapped resolver
     * @return partial resolver, which resolves everything
     */
    public static PartialResolver universalResolver(final PropertyResolver resolver) {
        return new UniversalResolver(resolver);
    }

    private static class UniversalResolver implements PartialResolver {
        private final PropertyResolver resolver;

        public UniversalResolver(PropertyResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public boolean canGet(Object target, PropertyAccess propertyAccess) {
            return true;
        }

        @Override
        public boolean canSet(Object target, PropertyAccess propertyAccess, Object value) {
            return true;
        }

        @Override
        public Object get(Object bean, PropertyAccess property) {
            return resolver.get(bean, property);
        }

        @Override
        public void set(Object bean, PropertyAccess property, Object value) {
            resolver.set(bean, property, value);
        }
    }
}
