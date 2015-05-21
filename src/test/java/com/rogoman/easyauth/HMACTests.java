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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACTests {
    @Test
    public void hmacDigestHappyPathTest() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String result = HMAC.hmacDigest("The quick brown fox jumps over the lazy dog", "key", "HmacSHA1");
        Assert.assertEquals("de7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9", result);
    }

    @Test
    public void hmacDigestEmptyArgumentsTest() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        try {
            HMAC.hmacDigest("", "", "HmacSHA1");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void hmacDigestNullParametersTest() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        try {
            HMAC.hmacDigest(null, "key", "HmacSHA1");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            HMAC.hmacDigest("Message", null, "HmacSHA1");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            HMAC.hmacDigest("Message", "key", null);
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }
}
