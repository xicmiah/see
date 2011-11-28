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
import see.parser.grammar.PropertyAccess;
import see.reactive.VariableSignal;

/**
 * Signal matcher/resolver.
 */
public interface SignalResolver {
    /**
     * Get signal, which corresponds to specific property of target bean.
     * Return empty {@link Optional}, if bean class or property is not supported.
     * @param target target object
     * @param lastProperty property on target object
     * @return corresponding signal, wrapped in {@link Optional}.
     */
    Optional<VariableSignal<Object>> getSignal(Object target, PropertyAccess lastProperty);
}
