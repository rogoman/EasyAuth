package com.rogoman.easyauth;

import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * An authenticator implementation using time-based tokens.
 */
public class TimeAuthenticator extends Authenticator {

    /**
     * The number of milliseconds in a second.
     */
    public static final int MILLIS_IN_SECOND = 1000;

    private static final int CHECK_BACK_INTERVALS = 5;

    private static final int CHECK_FORWARD_INTERVALS = 5;

    private final UsedCodesManager<String> usedCodeManager;
    private final int intervalSeconds;

    /**
     * Creates a new instance of the TimeAuthenticator class.
     *
     * @param usedCodesManager used codes manager
     * @param intervalSeconds  token generating interval in seconds
     */
    public TimeAuthenticator(final UsedCodesManager<String> usedCodesManager, final int intervalSeconds) {
        if (usedCodesManager == null) {
            throw new IllegalArgumentException("usedCodesManager cannot be null");
        }
        if (intervalSeconds <= 0) {
            throw new IllegalArgumentException("intervalSeconds parameter has to be positive");
        }

        this.usedCodeManager = usedCodesManager;
        this.intervalSeconds = intervalSeconds;
    }

    /**
     * Gets a new time-based code. Uses current datetime.
     *
     * @param secret secret used for generating the code
     * @return generated code
     */
    @Override
    public String getCode(final String secret) {
        if (StringUtils.isEmpty(secret)) {
            throw new IllegalArgumentException("secret cannot be null");
        }
        long interval = getInterval(System.currentTimeMillis() / MILLIS_IN_SECOND);
        return this.getCodeInternal(secret, interval);
    }

    /**
     * Gets a new time-based code.
     *
     * @param secret                    secret used for generating the code
     * @param currentEpochTimeInSeconds current Epoch time in seconds
     * @return generated code
     */
    public String getCode(final String secret, final long currentEpochTimeInSeconds) {
        if (StringUtils.isEmpty(secret)) {
            throw new IllegalArgumentException("secret cannot be null");
        }
        long interval = getInterval(currentEpochTimeInSeconds);
        return this.getCodeInternal(secret, interval);
    }

    /**
     * Checks if the provided code is valid for given secret key and user identifier. Current time is used for verification.
     *
     * @param secret         secret used for generating the code
     * @param code           generated code
     * @param userIdentifier user identifier
     * @return true if the code is valid and should be accepted
     */
    @Override
    public boolean checkCode(final String secret, final String code, final String userIdentifier) {
        if (StringUtils.isEmpty(secret)) {
            throw new IllegalArgumentException("secret cannot be null");
        }
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("code cannot be null");
        }

        Date baseTime = new Date();

        // We need to do this in constant time
        boolean codeMatch = false;
        for (int i = -CHECK_BACK_INTERVALS; i <= CHECK_FORWARD_INTERVALS; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baseTime);
            calendar.add(Calendar.SECOND, intervalSeconds * i);

            long currentEpochTime = calendar.getTimeInMillis() / MILLIS_IN_SECOND;
            long currentInterval = getInterval(currentEpochTime);

            if (linearTimeEquals(getCode(secret, currentEpochTime), code)
                    && !usedCodeManager.isCodeUsed(currentInterval, code, userIdentifier)) {
                codeMatch = true;
                usedCodeManager.addCode(currentInterval, code, userIdentifier);
                break;
            }
        }
        return codeMatch;
    }

    protected UsedCodesManager<String> getUsedCodeManager() {
        return usedCodeManager;
    }

    protected int getIntervalSeconds() {
        return intervalSeconds;
    }

    /**
     * Gets an interval from a given Epoch time in seconds.
     *
     * @param epochTimeInSeconds Epoch time in seconds
     * @return Interval to be used in calculations
     */
    private long getInterval(final long epochTimeInSeconds) {
        return epochTimeInSeconds / intervalSeconds;
    }
}
