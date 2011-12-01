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

package see.properties.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.beanutils.MethodUtils;
import see.exceptions.EvaluationException;
import see.functions.VarArgFunction;
import see.parser.grammar.PropertyAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;

public class MethodResolver extends ReadOnlyResolver {
    @Override
    public boolean canGet(@Nullable Object target, PropertyAccess propertyAccess) {
        if (target == null || !propertyAccess.value().hasLeft()) return false;

        final String methodName = propertyAccess.value().leftValue();
        return Iterables.any(asList(target.getClass().getMethods()), new Predicate<Method>() {
            @Override
            public boolean apply(Method input) {
                return methodName.equals(input.getName());
            }
        });
    }

    @Override
    public VarArgFunction<Object, ?> get(final Object bean, PropertyAccess property) {
        final String methodName = property.value().leftValue();

        return new VarArgFunction<Object, Object>() {
            @Override
            public Object apply(@Nonnull List<Object> input) {
                try {
                    return MethodUtils.invokeMethod(bean, methodName, Iterables.toArray(input, Object.class));
                } catch (NoSuchMethodException e) {
                    throw new ResolutionException(e);
                } catch (IllegalAccessException e) {
                    throw new ResolutionException(e);
                } catch (InvocationTargetException e) {
                    throw new ResolutionException(e);
                }
            }
        };
    }

    public static class ResolutionException extends EvaluationException {
        public ResolutionException(Throwable cause) {
            super(cause);
        }
    }
}
