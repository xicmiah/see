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

package see.functions.opt;

import com.google.common.base.Preconditions;
import see.functions.VarArgFunction;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static com.google.common.cache.CacheLoader.from;
import static see.util.FunctionUtils.toFunction;
import static see.util.FunctionUtils.toVarArg;

public abstract class Memoize {
    public static <A, R> VarArgFunction<A, R> memoize(final VarArgFunction<A, R> f) {
        return toVarArg(newBuilder().build(from(toFunction(f))));
    }

    public static <A, R> VarArgFunction<VarArgFunction<A, R>, VarArgFunction<A, R>> memoizeFunction() {
        return new MemoizeFunction<A, R>();
    }

    private static class MemoizeFunction<A, R> implements VarArgFunction<VarArgFunction<A, R>, VarArgFunction<A, R>> {
        @Override
        public VarArgFunction<A, R> apply(@Nonnull List<VarArgFunction<A, R>> input) {
            Preconditions.checkArgument(input.size() == 1, "Memoize takes one argument");

            return memoize(input.get(0));
        }
    }
}
