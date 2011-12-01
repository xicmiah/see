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
import com.google.common.collect.Iterables;

import java.util.NoSuchElementException;

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
     * Get right instance.
     * @return iterable containing one element if container holds right instance, empty otherwise
     */
    public Iterable<R> right() {
        return right;
    }

    /**
     * Check, if container holds left instance.
     * @return true if this container holds left instance
     */
    public boolean hasLeft() {
        return left.iterator().hasNext();
    }

    /**
     * Check, if container holds right instance.
     * @return true if this container holds right instance
     */
    public boolean hasRight() {
        return right.iterator().hasNext();
    }

    /**
     * Get left value, throwing exception if value is not present.
     * @return left value
     * @throws NoSuchElementException if container doesn't hold left value
     */
    public L leftValue() {
        return Iterables.getOnlyElement(left);
    }

    /**
     * Get right value, throwing exception if value is not present.
     * @return right value
     * @throws NoSuchElementException if container doesn't hold right value
     */
    public R rightValue() {
        return Iterables.getOnlyElement(right);
    }
}
