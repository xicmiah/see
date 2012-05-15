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

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import see.parser.grammar.PropertyAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


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

    /**
     * Construct a partial resolver from a collection of other partial resolvers.
     * Constructed resolver delegates to first resolver, which can complete target operation.
     *
     * @param resolvers partial resolvers to aggregate
     * @return constructed aggregating resolver
     */
    public static PartialResolver aggregate(final Iterable<? extends PartialResolver> resolvers) {
        return new PartialAggregator(resolvers);
    }

    /**
     * Construct a partial resolver from a collection of other partial resolvers.
     * Constructed resolver delegates to first resolver, which can complete target operation.
     *
     * @param resolvers partial resolvers to aggregate
     * @return constructed aggregating resolver
     */
    public static PartialResolver aggregate(PartialResolver... resolvers) {
        return aggregate(ImmutableList.copyOf(resolvers));
    }

    private static class UniversalResolver implements PartialResolver {
        private final PropertyResolver resolver;

        public UniversalResolver(PropertyResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public boolean canGet(Object target, @Nonnull PropertyAccess propertyAccess) {
            return true;
        }

        @Override
        public boolean canSet(Object target, @Nonnull PropertyAccess propertyAccess, Object value) {
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

    private static class PartialAggregator implements PartialResolver {
        private final Iterable<? extends PartialResolver> resolvers;

        public PartialAggregator(Iterable<? extends PartialResolver> resolvers) {
            this.resolvers = resolvers;
        }

        @Override
        public boolean canGet(@Nullable final Object target, @Nonnull final PropertyAccess propertyAccess) {
            return Iterables.any(resolvers, new CanGetPredicate(target, propertyAccess));
        }

        @Override
        public boolean canSet(@Nullable final Object target, @Nonnull final PropertyAccess propertyAccess, final Object value) {
            return Iterables.any(resolvers, new CanSetPredicate(target, propertyAccess, value));
        }

        @Override
        public Object get(Object bean, PropertyAccess property) {
            return Iterables.find(resolvers, new CanGetPredicate(bean, property)).get(bean, property);
        }

        @Override
        public void set(Object bean, PropertyAccess property, Object value) {
            Iterables.find(resolvers, new CanSetPredicate(bean, property, value)).set(bean, property, value);
        }
    }

    private static class CanGetPredicate implements Predicate<PartialResolver> {
        private final Object target;
        private final PropertyAccess propertyAccess;

        public CanGetPredicate(Object target, PropertyAccess propertyAccess) {
            this.target = target;
            this.propertyAccess = propertyAccess;
        }

        @Override
        public boolean apply(PartialResolver input) {
            return input.canGet(target, propertyAccess);
        }
    }

    private static class CanSetPredicate implements Predicate<PartialResolver> {
        private final Object target;
        private final PropertyAccess propertyAccess;
        private final Object value;

        public CanSetPredicate(Object target, PropertyAccess propertyAccess, Object value) {
            this.target = target;
            this.propertyAccess = propertyAccess;
            this.value = value;
        }

        @Override
        public boolean apply(PartialResolver input) {
            return input.canSet(target, propertyAccess, value);
        }
    }
}
