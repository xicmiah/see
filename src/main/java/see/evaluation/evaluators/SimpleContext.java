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

package see.evaluation.evaluators;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import see.evaluation.Context;
import see.evaluation.Scope;

public class SimpleContext implements Context {

    private final Scope scope;
    private final ClassToInstanceMap<Object> services;

    private SimpleContext(Scope scope, ClassToInstanceMap<Object> services) {
        this.scope = scope;
        this.services = ImmutableClassToInstanceMap.copyOf(services);
    }

    public static SimpleContext create(Scope scope, ClassToInstanceMap<Object> services) {
        return new SimpleContext(scope, services);
    }


    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public ClassToInstanceMap<Object> getServices() {
        return services;
    }
}
