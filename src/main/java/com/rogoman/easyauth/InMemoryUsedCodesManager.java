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

import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Queue;
import java.util.Timer;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A manager of used codes that keeps the used ones in memory.
 * The used codes are kept for a configurable time.
 */
public class InMemoryUsedCodesManager implements UsedCodesManager<String> {
    private static class UsedCode {
        private Date useDate;
        private long timestamp;
        private String code;
        private String userId;

        /**
         * Creates a new instance of UsedCode.
         *
         * @param timestamp timestamp of the code usage
         * @param code      code
         * @param userId    user identifier
         */
        public UsedCode(final long timestamp, final String code, final String userId) {
            if (StringUtils.isEmpty(code)) {
                throw new IllegalArgumentException("code is empty");
            }

            this.useDate = new Date();
            this.timestamp = timestamp;
            this.code = code;
            this.userId = userId;
        }

        public Date getUseDate() {
            return useDate;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(final long timestamp) {
            this.timestamp = timestamp;
        }

        public String getCode() {
            return code;
        }

        public void setCode(final String code) {
            this.code = code;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            UsedCode usedCode = (UsedCode) o;

            if (timestamp != usedCode.timestamp) {
                return false;
            }
            if (!code.equals(usedCode.code)) {
                return false;
            }
            if (userId != null ? !userId.equals(usedCode.userId) : usedCode.userId != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (timestamp ^ (timestamp >>> 32));
            result = 31 * result + code.hashCode();
            result = 31 * result + (userId != null ? userId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "UsedCode{"
                    + "timestamp="
                    + timestamp
                    + ", code='"
                    + code
                    + '\''
                    + '}';
        }
    }

    private static final int MILLISECONDS_IN_SECOND = 1000;

    private static final long CLEANUP_PERIOD_IN_MILLISECONDS = 60 * MILLISECONDS_IN_SECOND;

    private static final int CLEAN_OLDER_THAN_IN_MILLISECONDS = 5 * 60 * MILLISECONDS_IN_SECOND;

    private final Queue<UsedCode> codeQueue = new LinkedList<>();
    private final Set<UsedCode> codeSet = new HashSet<>();
    private final Lock syncLock = new ReentrantLock(true);
    private final Timer timer = new Timer();
    private long cleanupTime;
    private int cleanupAge;

    /**
     * Constructs a new InMemoryUsedCodesManager using default values for the cleanup time and cleanup age.
     */
    public InMemoryUsedCodesManager() {
        this(CLEANUP_PERIOD_IN_MILLISECONDS, CLEAN_OLDER_THAN_IN_MILLISECONDS);
    }

    /**
     * Constructs a new InMemoryUsedCodesManager using provided values for the cleanup time and cleanup age.
     *
     * @param cleanupTimeInSeconds cleanup time in seconds
     * @param cleanupAgeInSeconds  cleanup age in seconds
     */
    public InMemoryUsedCodesManager(final long cleanupTimeInSeconds, final int cleanupAgeInSeconds) {
        this.cleanupTime = cleanupTimeInSeconds * MILLISECONDS_IN_SECOND;
        this.cleanupAge = cleanupAgeInSeconds * MILLISECONDS_IN_SECOND;
        setupQueueCleanup();
    }

    @Override
    public void addCode(final long timestamp, final String code, final String userId) {
        UsedCode usedCode = new UsedCode(timestamp, code, userId);
        try {
            syncLock.lock();
            this.codeQueue.add(usedCode);
            this.codeSet.add(usedCode);
        } finally {
            syncLock.unlock();
        }
    }

    @Override
    public boolean isCodeUsed(final long timestamp, final String code, final String userId) {
        UsedCode usedCode = new UsedCode(timestamp, code, userId);
        try {
            syncLock.lock();
            return this.codeSet.contains(usedCode);
        } finally {
            syncLock.unlock();
        }
    }

    /**
     * Cleans up the data structures of old entries.
     */
    private void queueCleanup() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, -this.cleanupAge);
        Date timeToClean = cal.getTime();
        try {
            syncLock.lock();
            while (!codeQueue.isEmpty() && codeQueue.peek().getUseDate().before(timeToClean)) {
                UsedCode usedCode = codeQueue.remove();
                codeSet.remove(usedCode);
            }
        } finally {
            syncLock.unlock();
        }
    }

    /**
     * Sets up the background thread to clean up the data structures periodically.
     */
    private void setupQueueCleanup() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                queueCleanup();
            }
        }, cleanupTime, cleanupTime);
    }
}
