/*
 * Copyright 2012 Vasily Shiyan
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

package see.util;

import com.google.common.base.Supplier;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Suppliers {
    private Suppliers() {}

    /**
     * Create supplier for {@link AtomicReference}
     * @param reference source reference
     * @param <T> value type
     * @return created supplier
     */
    public static <T> Supplier<T> fromAtomicReference(final AtomicReference<T> reference) {
        return new Supplier<T>() {
            @Override
            public T get() {
                return reference.get();
            }
        };
    }
}
