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
