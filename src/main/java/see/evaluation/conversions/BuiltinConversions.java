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

package see.evaluation.conversions;

import see.evaluation.ToFunction;
import see.functions.ContextCurriedFunction;

import javax.annotation.Nonnull;

import static see.evaluation.conversions.FunctionConversions.concat;

public abstract class BuiltinConversions {
    public static ToFunction varArgIdentity() {
        return new VarArgIdentity();
    }

    public static ToFunction identity() {
        return new ToFunctionIdentity();
    }

    public static ToFunction all() {
        return concat(identity(), varArgIdentity());
    }

    private static class ToFunctionIdentity implements ToFunction {
        @Nonnull
        @Override
        public ContextCurriedFunction<Object, ?> apply(@Nonnull Object input) {
            return (ContextCurriedFunction<Object, ?>) input;
        }

        @Override
        public boolean isDefinedAt(Object input) {
            return input instanceof ContextCurriedFunction<?, ?>;
        }
    }
}
