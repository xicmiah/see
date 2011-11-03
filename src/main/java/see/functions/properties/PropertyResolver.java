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

import see.parser.grammar.PropertyAccess;

public interface PropertyResolver {

    /**
     * Get property via specified path
     * @param bean target bean
     * @param property property descriptor
     * @return resolved property
     */
    Object get(Object bean, PropertyAccess property);

    /**
     * Set property to target bean
     * @param bean target bean
     * @param property property descriptor
     * @param value new property value
     */
    void set(Object bean, PropertyAccess property, Object value);
}
