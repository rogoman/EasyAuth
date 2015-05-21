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
 * Exception wrapping all other exceptions thrown by internals of the authenticator components.
 */
public class AuthenticatorException extends Exception {

    /**
     * Creates a new AuthenticatorException instance.
     *
     * @param message message
     * @param cause   internal cause
     */
    public AuthenticatorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
