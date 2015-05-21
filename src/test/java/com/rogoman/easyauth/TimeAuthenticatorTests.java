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

import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidKeyException;

public class TimeAuthenticatorTests {

    private class UsedCodesManagerImpl implements UsedCodesManager<String> {

        private boolean codeUsed;

        public void setCodeUsedResult(boolean result) {
            this.codeUsed = result;
        }

        @Override
        public void addCode(long timestamp, String code, String userId) {
            this.codeUsed = true;
        }

        @Override
        public boolean isCodeUsed(long timestamp, String code, String userId) {
            return this.codeUsed;
        }
    }

    private static final int INTERVAL_WINDOW_IN_SECONDS = 30;

    @Test
    public void getCodeAndVerifyHappyPathTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = Authenticator.generateKey();
        Assert.assertNotNull(secret);

        String code = target.getCode(secret);
        Assert.assertNotNull(code);

        boolean result = target.checkCode(secret, code, "userIdentifier");
        Assert.assertTrue(result);
        Assert.assertTrue(manager.isCodeUsed(0, null, null));
    }

    @Test
    public void getCodeFromPreviousIntervalTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = Authenticator.generateKey();
        Assert.assertNotNull(secret);

        String code = target.getCode(secret, System.currentTimeMillis() / TimeAuthenticator.MILLIS_IN_SECOND - INTERVAL_WINDOW_IN_SECONDS);
        Assert.assertNotNull(code);

        boolean result = target.checkCode(secret, code, "userIdentifier");
        Assert.assertTrue(result);
        Assert.assertTrue(manager.isCodeUsed(0, null, null));
    }

    @Test
    public void getCodeFromNextIntervalTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = Authenticator.generateKey();
        Assert.assertNotNull(secret);

        String code = target.getCode(secret, System.currentTimeMillis() / TimeAuthenticator.MILLIS_IN_SECOND + INTERVAL_WINDOW_IN_SECONDS);
        Assert.assertNotNull(code);

        boolean result = target.checkCode(secret, code, "userIdentifier");
        Assert.assertTrue(result);
        Assert.assertTrue(manager.isCodeUsed(0, null, null));
    }

    @Test
    public void getCodeFromTooManyIntervalsBeforeTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = Authenticator.generateKey();
        Assert.assertNotNull(secret);

        String code = target.getCode(secret, System.currentTimeMillis() / TimeAuthenticator.MILLIS_IN_SECOND - 8 * INTERVAL_WINDOW_IN_SECONDS);
        Assert.assertNotNull(code);

        boolean result = target.checkCode(secret, code, "userIdentifier");
        Assert.assertFalse(result);
        Assert.assertFalse(manager.isCodeUsed(0, null, null));
    }

    @Test
    public void getCodeFromTooManyIntervalsAfterTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = Authenticator.generateKey();
        Assert.assertNotNull(secret);

        String code = target.getCode(secret, System.currentTimeMillis() / TimeAuthenticator.MILLIS_IN_SECOND + 7 * INTERVAL_WINDOW_IN_SECONDS);
        Assert.assertNotNull(code);

        boolean result = target.checkCode(secret, code, "userIdentifier");
        Assert.assertFalse(result);
        Assert.assertFalse(manager.isCodeUsed(0, null, null));
    }

    @Test
    public void getCodeFromInvalidSecretTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = "BAD SECRET";

        try {
            target.getCode(secret);
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void checkCodeFromInvalidSecretTest() {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = "BAD SECRET";
        String code = "badCode";

        try {
            target.checkCode(secret, code, "userIdentifier");
            Assert.fail("No Exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void getCodeIfAlreadyUsedTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        manager.setCodeUsedResult(true);
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = Authenticator.generateKey();
        Assert.assertNotNull(secret);

        String code = target.getCode(secret, System.currentTimeMillis() / TimeAuthenticator.MILLIS_IN_SECOND);
        Assert.assertNotNull(code);

        boolean result = target.checkCode(secret, code, "userIdentifier");
        Assert.assertFalse(result);
        Assert.assertTrue(manager.isCodeUsed(0, null, null));
    }

    @Test
    public void adHocTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        manager.setCodeUsedResult(false);
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = "AAAAAAAAAAAAAAAA";

        String code = target.getCode(secret, 1415618880L);
        Assert.assertEquals("898106", code);
    }

    @Test
    public void adHocLowercaseTest() throws AuthenticatorException, InvalidKeyException {
        UsedCodesManagerImpl manager = new UsedCodesManagerImpl();
        manager.setCodeUsedResult(false);
        TimeAuthenticator target = new TimeAuthenticator(manager, INTERVAL_WINDOW_IN_SECONDS);
        String secret = "aaaaaaaaaaaaaaaa";

        String code = target.getCode(secret, 1415618880L);
        Assert.assertEquals("898106", code);
    }
}
