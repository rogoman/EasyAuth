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

public class Base32EncodingTests {
    @Test
    public void toBytesEmptyInputTest() {
        try {
            Base32Encoding.toBytes(null);
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            Base32Encoding.toBytes("");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void toBytesNormalInputTest() throws Exception {
        byte[] output = Base32Encoding.toBytes("KNXW2ZJANFXHA5LU");
        String result = new String(output, "UTF-8");
        Assert.assertEquals("Some input", result);
    }

    @Test
    public void toBytesIllegalCharactersTest() {
        try {
            Base32Encoding.toBytes("Some non base32 input");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void toStringNullInputTest() {
        try {
            Base32Encoding.toString(null);
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            Base32Encoding.toString(new byte[0]);
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void toStringNormalInputTest() throws Exception {
        byte[] input = "Some input".getBytes("UTF-8");
        String result = Base32Encoding.toString(input);
        Assert.assertEquals("KNXW2ZJANFXHA5LU", result);
    }
}
