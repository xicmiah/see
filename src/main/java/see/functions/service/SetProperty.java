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

package see.functions.service;

import com.google.common.base.Preconditions;
import org.apache.commons.beanutils.PropertyUtils;
import see.exceptions.EvaluationException;
import see.functions.VarArgFunction;

import java.util.List;

public class SetProperty implements VarArgFunction<Object, Object> {
    @Override
    public Object apply(List<Object> input) {
        Preconditions.checkArgument(input.size() == 3, "SetProperty takes three arguments");

        Object bean = input.get(0);
        String property = (String) input.get(1);
        Object value = input.get(2);

        try {
            PropertyUtils.setProperty(bean, property, value);
        } catch (Exception e) {
            throw new EvaluationException("Couldn't write property", e);
        }

        return value;
    }

    @Override
    public String toString() {
        return "set";
    }
}
