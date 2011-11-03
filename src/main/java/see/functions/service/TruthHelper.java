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

import static see.functions.bool.BooleanCastHelper.toBoolean;

/**
 * Helper for implicit casting to boolean.
 */
public abstract class TruthHelper {
    private TruthHelper() {}

    /**
     * Cast the object to boolean.
     * Casting is performed by the following rules:
     *  zero is false, all other numbers are t
     *
     * @param condition object to evaluate
     * @return evaluation result
     */
    public static boolean isTrue(Object condition) {
        if (condition instanceof Boolean) {
            return (Boolean) condition;
        } else if (condition instanceof Number) {
            return toBoolean((Number) condition);
        } else {
            throw new IllegalArgumentException("Cannot evaluate condition " + condition);
        }
    }

}
