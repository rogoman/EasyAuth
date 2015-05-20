package com.rogoman.easyauth;

/**
 * A very simple implementation of the UsedCodesManager. Use this class ff you don't need to
 * ensure that a given code is used only once and can be used multiple times within the current time quant.
 */
public class SimpleUsedCodesManager implements UsedCodesManager<String> {
    @Override
    public void addCode(final long timestamp, final String code, final String userId) {
        // do nothing
    }

    @Override
    public boolean isCodeUsed(final long timestamp, final String code, final String userId) {
        return false; // never reject a code
    }
}
