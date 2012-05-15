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

import com.google.common.collect.Iterables;
import see.parser.grammar.PropertyAccess;
import see.properties.PartialResolver;
import see.util.Either;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class IterableResolver implements PartialResolver {
    @Override
    public boolean canGet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess) {
        return target instanceof Iterable<?> && isListIndex(propertyAccess);
    }

    @Override
    public boolean canSet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess, Object value) {
         return target instanceof List<?> && isListIndex(propertyAccess);
    }

    private boolean isListIndex(PropertyAccess propertyAccess) {
        Either<String, Object> value = propertyAccess.value();
        return value.hasRight() && value.rightValue() instanceof Number;
    }

    @Override
    public Object get(Object bean, PropertyAccess property) {
        Iterable<?> list = (Iterable<?>) bean;
        int index = getIndex(property);
        return Iterables.get(list, index);
    }

    private int getIndex(PropertyAccess property) {
        return ((Number) property.value().rightValue()).intValue();
    }

    @Override
    public void set(Object bean, PropertyAccess property, Object value) {
        List<Object> list = (List<Object>) bean;
        int index = getIndex(property);
        list.set(index, value);
    }
}
