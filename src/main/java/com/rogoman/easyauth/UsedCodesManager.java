/*
 * Copyright 2015 Tomasz Rogozik
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.rogoman.easyauth;

/**
 * An interface of a used codes manager.
 *
 * @param <T> A type of user identifiers.
 */
public interface UsedCodesManager<T> {

    /**
     * Adds a challenge usage to the cache.
     *
     * @param timestamp Used timestamp
     * @param code      Used code
     * @param userId    The user identifier
     */
    void addCode(long timestamp, String code, T userId);

    /**
     * Checks if code was previously used.
     *
     * @param timestamp Used timestamp
     * @param code      Used code
     * @param userId    The user identifier
     * @return true if the code has already been used for this user
     */
    boolean isCodeUsed(long timestamp, String code, T userId);
}
