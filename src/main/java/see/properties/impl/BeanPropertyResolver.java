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

import com.google.common.base.Throwables;
import org.apache.commons.beanutils.PropertyUtils;
import see.parser.grammar.PropertyAccess;
import see.properties.PartialResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BeanPropertyResolver implements PartialResolver {

    @Override
    public boolean canGet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess) {
        return isBeanProperty(propertyAccess) && PropertyUtils.isReadable(target, getPropertyName(propertyAccess));
    }

    @Override
    public boolean canSet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess, Object value) {
        return isBeanProperty(propertyAccess) && PropertyUtils.isWriteable(target, getPropertyName(propertyAccess));
    }

    @Override
    public Object get(Object bean, PropertyAccess property) {
        try {
            return PropertyUtils.getSimpleProperty(bean, getPropertyName(property));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void set(Object bean, PropertyAccess property, Object value) {
        try {
            PropertyUtils.setSimpleProperty(bean, getPropertyName(property), value);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private String getPropertyName(PropertyAccess propertyAccess) {
        return (String) propertyAccess.mergedValue();
    }

    private boolean isBeanProperty(PropertyAccess propertyAccess) {
        return propertyAccess.mergedValue() instanceof String;
    }

}
