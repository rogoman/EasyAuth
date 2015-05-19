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
