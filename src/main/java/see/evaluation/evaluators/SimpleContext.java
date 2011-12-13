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

import com.google.common.base.Preconditions;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableMap;
import see.evaluation.Context;

import java.util.Map;

import static com.google.common.base.Optional.fromNullable;

public class SimpleContext implements Context {

    private final Map<String, Object> mutable;
    private Map<String, ?> constants;
    private final ClassToInstanceMap<Object> service;

    public SimpleContext(Map<String, Object> mutable, Map<String, ?> constants, ClassToInstanceMap<Object> service) {
        this.mutable = mutable;
        this.constants = ImmutableMap.copyOf(constants);
        this.service = ImmutableClassToInstanceMap.copyOf(service);
    }

    @Override
    public Object get(String key) {
        return fromNullable(mutable.get(key)).or(fromNullable(constants.get(key))).orNull();
    }

    @Override
    public void put(String key, Object value) {
        Preconditions.checkArgument(!constants.containsKey(key), "Cannot reassign a constant");

        mutable.put(key, value);
    }

    @Override
    public Map<String, ?> asMap() {
        return ImmutableMap.<String, Object>builder().putAll(constants).putAll(mutable).build();
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return service.getInstance(serviceClass);
    }

    public void addConstant(String name, Object constant) {
        constants = ImmutableMap.<String, Object>builder().putAll(constants).put(name, constant).build();
    }

    public static SimpleContext fromMutable(Map<String, Object> mutablePart) {
        Map<String, Object> constants = ImmutableMap.of();
        ImmutableClassToInstanceMap<Object> services = ImmutableClassToInstanceMap.builder().build();
        return new SimpleContext(mutablePart, constants, services);

    }
}
