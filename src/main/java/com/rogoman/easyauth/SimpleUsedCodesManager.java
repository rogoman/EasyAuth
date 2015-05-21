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
