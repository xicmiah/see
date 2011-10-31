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

import com.google.common.base.Preconditions;
import see.functions.VarArgFunction;
import see.parser.grammar.PropertyAccess;

import java.util.List;

public class MakeTarget implements VarArgFunction<Object, PropertyAccess.Target> {
    @Override
    public PropertyAccess.Target apply(List<Object> input) {
        Preconditions.checkArgument(input.size() == 1, "MakeTarget takes one argument");
        return new PropertyAccess.Target(input.get(0));
    }

    @Override
    public String toString() {
        return "prop.target";
    }
}
