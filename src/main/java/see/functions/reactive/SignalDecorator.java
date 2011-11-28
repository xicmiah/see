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

package see.functions.reactive;

import com.google.common.base.Optional;
import see.functions.properties.PropertyResolver;
import see.parser.grammar.PropertyAccess;
import see.reactive.Signal;
import see.reactive.VariableSignal;

/**
 * Decorator for {@link see.functions.properties.ChainResolver}, which mirrors all operations
 * to corresponding {@link Signal} instance, supplied by {@link SignalResolver}.
 */
public class SignalDecorator implements PropertyResolver {
    /**
     * Actual property resolver.
     */
    private PropertyResolver delegate;
    private SignalResolver resolver;

    public SignalDecorator(PropertyResolver delegate, SignalResolver resolver) {
        this.delegate = delegate;
        this.resolver = resolver;
    }

    @Override
    public Object get(Object bean, PropertyAccess property) {
        return delegate.get(bean, property);
    }

    @Override
    public void set(Object bean, PropertyAccess property, Object value) {
        delegate.set(bean, property, value);

        Optional<VariableSignal<Object>> signal = resolver.getSignal(bean, property);
        if (signal.isPresent()) {
            signal.get().update(value);
        }
    }


}
