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

import java.util.List;

/**
 * Property resolver for a full chain of properties.
 */
public interface ChainResolver {
    /**
     * Get a object from property chain
     * @param target target object for property resolution
     * @param chain chain of property descriptors
     * @return resolved object
     */
    Object get(Object target, List<? extends PropertyAccess> chain);

    /**
     * Set a property at end of chain
     * @param target target object for property resolution
     * @param chain chain of property descriptors
     * @param value new property value
     */
    void set(Object target, List<? extends PropertyAccess> chain, Object value);
}
