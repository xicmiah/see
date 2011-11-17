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

package see.util;

import com.google.common.collect.ImmutableList;

import static com.google.common.collect.ImmutableList.of;

/**
 * Container, which holds one instance of either type {@link L} or type {@link R}.
 * @param <L> left type
 * @param <R> right type
 */
public class Either<L,R> {

    private final Iterable<L> left;
    private final Iterable<R> right;

    private Either(Iterable<L> left, Iterable<R> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Create container holding left instance.
     *
     * @param left left instance to hold
     * @param <L> left type
     * @param <R> right type
     * @return created container
     */
    public static <L, R> Either<L, R> left(L left) {
        return new Either<L, R>(of(left), ImmutableList.<R>of());
    }

    /**
     * Create container holding right instance.
     *
     * @param right right instance to hold
     * @param <L> left type
     * @param <R> right type
     * @return created container
     */
    public static <L, R> Either<L, R> right(R right) {
        return new Either<L, R>(ImmutableList.<L>of(), of(right));
    }

    /**
     * Get left instance.
     * @return iterable containing one element if container holds left instance, empty otherwise
     */
    public Iterable<L> left() {
        return left;
    }

    /**
     * Get left instance.
     * @return iterable containing one element if container holds left instance, empty otherwise
     */
    public Iterable<R> right() {
        return right;
    }
}
