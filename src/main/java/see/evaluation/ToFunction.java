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

package see.evaluation;

import see.functions.ContextCurriedFunction;
import see.functions.PartialFunction;

import javax.annotation.Nonnull;

public interface ToFunction extends PartialFunction<Object, ContextCurriedFunction<Object, ?>> {

    /**
     * Try to convert given object to function.
     *
     * @param input object to convert
     * @return function
     */
    @Override
    @Nonnull
    ContextCurriedFunction<Object, ?> apply(@Nonnull Object input);

    /**
     * Return, if this object can be converted to function.
     * @param input object to test
     * @return true, if {@link #apply(Object)} will return correct function
     */
    @Override
    boolean isDefinedAt(Object input);
}
