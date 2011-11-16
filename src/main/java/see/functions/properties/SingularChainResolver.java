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
import see.util.Reduce;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static see.util.Reduce.fold;

/**
 * ChainResolver implementation, which delegates work to PropertyResolver (single property resolution)
 */
public class SingularChainResolver implements ChainResolver {

    private final PropertyResolver resolver;

    public SingularChainResolver(PropertyResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Object get(Object target, List<? extends PropertyAccess> chain) {
        return fold(target, chain, new Reduce.FoldFunction<PropertyAccess, Object>() {
            @Override
            public Object apply(Object prev, PropertyAccess arg) {
                return resolver.get(prev, arg);
            }
        });
    }

    @Override
    public void set(Object target, List<? extends PropertyAccess> chain, Object value) {
        Object lastbean = get(target, chain.subList(0, chain.size() - 1));

        resolver.set(lastbean, getLast(chain), value);
    }
}
