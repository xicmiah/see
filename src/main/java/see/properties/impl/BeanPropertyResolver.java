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
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.PropertyUtils;
import see.parser.grammar.PropertyAccess;
import see.properties.PartialResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.PropertyDescriptor;
import java.util.Map;

import static com.google.common.collect.ImmutableSet.copyOf;

public class BeanPropertyResolver implements PartialResolver {

    private Cache<Class<?>, Map<String, PropertyDescriptor>> properties = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, Map<String, PropertyDescriptor>>() {
        @Override
        public Map<String, PropertyDescriptor> load(Class<?> key) throws Exception {
            Iterable<PropertyDescriptor> descriptors = copyOf(PropertyUtils.getPropertyDescriptors(key));

            ImmutableMap.Builder<String, PropertyDescriptor> builder = ImmutableMap.builder();
            for (PropertyDescriptor descriptor : descriptors) {
                builder.put(descriptor.getName(), descriptor);
            }
            return builder.build();
        }
    });

    @Override
    public boolean canGet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess) {
        return isBeanProperty(propertyAccess) && properties.apply(target.getClass()).containsKey(getPropertyName(propertyAccess));
    }

    @Override
    public boolean canSet(@Nullable Object target, @Nonnull PropertyAccess propertyAccess, Object value) {
        return isBeanProperty(propertyAccess) && getDescriptor(target, propertyAccess).getWriteMethod() != null;
    }

    @Override
    public Object get(Object bean, PropertyAccess property) {
        try {
            return getDescriptor(bean, property).getReadMethod().invoke(bean);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void set(Object bean, PropertyAccess property, Object value) {
        try {
            getDescriptor(bean, property).getWriteMethod().invoke(bean, value);
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

    private PropertyDescriptor getDescriptor(Object bean, PropertyAccess property) {
        return properties.apply(bean.getClass()).get(getPropertyName(property));
    }

}
