/*
 * Copyright 2012 Vasily Shiyan
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

package see.properties.impl;

import see.parser.grammar.PropertyAccess;
import see.properties.PartialResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class MapResolver implements PartialResolver {
    @Override
    public boolean canGet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess) {
        return target instanceof Map<?, ?>;
    }

    @Override
    public boolean canSet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess, Object value) {
        return target instanceof Map<?, ?>;
    }

    @Override
    public Object get(Object bean, PropertyAccess property) {
        Map<Object, ?> map = (Map<Object, ?>) bean;
        return map.get(property.mergedValue());
    }

    @Override
    public void set(Object bean, PropertyAccess property, Object value) {
        Map<Object, Object> map = (Map<Object, Object>) bean;
        map.put(property.mergedValue(), value);
    }
}
