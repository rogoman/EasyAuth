package com.rogoman.easyauth;

import org.junit.Assert;
import org.junit.Test;

public class InMemoryUsedCodesManagerTests {
    @Test
    public void addCodeCheckCodeTest() {
        InMemoryUsedCodesManager target = new InMemoryUsedCodesManager();
        target.addCode(123, "code", "userId");
        boolean result = target.isCodeUsed(123, "code", "userId");
        Assert.assertTrue(result);
    }

    @Test
    public void addCodeRunCleanupCheckCodeTest() throws InterruptedException {
        InMemoryUsedCodesManager target = new InMemoryUsedCodesManager(1, 1);
        target.addCode(123, "code", "userId");
        Thread.sleep(3000);
        boolean result = target.isCodeUsed(123, "code", "userId");
        Assert.assertFalse(result);
    }

    @Test
    public void addCodeCheckCodeTwiceTest() {
        InMemoryUsedCodesManager target = new InMemoryUsedCodesManager();
        target.addCode(123, "code", "userId");

        boolean result = target.isCodeUsed(123, "code", "userId");
        Assert.assertTrue(result);

        result = target.isCodeUsed(123, "code", "userId");
        Assert.assertTrue(result);
    }

    @Test
    public void addCodeArgumentValidationTest() {
        InMemoryUsedCodesManager target = new InMemoryUsedCodesManager();
        try {
            target.addCode(123, null, "userId");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void checkCodeArgumentValidationTest() {
        InMemoryUsedCodesManager target = new InMemoryUsedCodesManager();
        try {
            target.isCodeUsed(123, null, "userId");
            Assert.fail("no exception thrown");
        } catch (IllegalArgumentException e) {
        }
    }
}
