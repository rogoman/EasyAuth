package com.rogoman.easyauth;

import org.apache.commons.lang.StringUtils;

/**
 * Represents BASE32 encoding used by the Google Authenticator algorithm.
 */
final class Base32Encoding {

    /**
     * A private constructor to prevent from instantiating this class.
     */
    private Base32Encoding() {
    }

    /**
     * Converts the BASE32 string input to a byte array.
     *
     * @param input BASE32 string input
     * @return byte array
     */
    static byte[] toBytes(final String input) {
        if (StringUtils.isEmpty(input)) {
            throw new IllegalArgumentException("input");
        }

        String cleanInput = input.replaceAll("[=]+$", ""); //remove padding characters
        int byteCount = cleanInput.length() * 5 / 8; //this must be TRUNCATED
        byte[] returnArray = new byte[byteCount];

        byte curByte = 0, bitsRemaining = 8;
        int mask, arrayIndex = 0;

        for (char c : cleanInput.toCharArray()) {
            int cValue = charToValue(c);

            if (bitsRemaining > 5) {
                mask = cValue << (bitsRemaining - 5);
                curByte = (byte) (curByte | mask);
                bitsRemaining -= 5;
            } else {
                mask = cValue >> (5 - bitsRemaining);
                curByte = (byte) (curByte | mask);
                returnArray[arrayIndex++] = curByte;
                curByte = (byte) (cValue << (3 + bitsRemaining));
                bitsRemaining += 3;
            }
        }

        //if we didn't end with a full byte
        if (arrayIndex != byteCount) {
            returnArray[arrayIndex] = curByte;
        }

        return returnArray;
    }

    /**
     * Converts a byte array to a BASE32 string.
     *
     * @param input byte array
     * @return BASE32 string
     */
    static String toString(final byte[] input) {
        if (input == null || input.length == 0) {
            throw new IllegalArgumentException("input");
        }

        int charCount = (int) Math.ceil(input.length / 5d) * 8;
        char[] returnArray = new char[charCount];

        byte nextChar = 0, bitsRemaining = 5;
        int arrayIndex = 0;

        for (byte b : input) {
            nextChar = (byte) (nextChar | (b >> (8 - bitsRemaining)));
            returnArray[arrayIndex++] = valueToChar(nextChar);

            if (bitsRemaining < 4) {
                nextChar = (byte) ((b >> (3 - bitsRemaining)) & 31);
                returnArray[arrayIndex++] = valueToChar(nextChar);
                bitsRemaining += 5;
            }

            bitsRemaining -= 3;
            nextChar = (byte) ((b << bitsRemaining) & 31);
        }

        //if we didn't end with a full char
        if (arrayIndex != charCount) {
            returnArray[arrayIndex++] = valueToChar(nextChar);
            while (arrayIndex != charCount) {
                returnArray[arrayIndex++] = '='; //padding
            }
        }

        return new String(returnArray);
    }

    /**
     * Converts a single character to a represented BASE32 value.
     *
     * @param c character to convert
     * @return represented value
     */
    private static int charToValue(final char c) {
        int value = (int) c;

        //65-90 == uppercase letters
        if (value < 91 && value > 64) {
            return value - 65;
        }
        //50-55 == numbers 2-7
        if (value < 56 && value > 49) {
            return value - 24;
        }
        //97-122 == lowercase letters
        if (value < 123 && value > 96) {
            return value - 97;
        }

        throw new IllegalArgumentException("Character is not a Base32 character.");
    }

    /**
     * Converts a single byte value to a BASE32 representation.
     *
     * @param b single byte
     * @return BASE32 representation
     */
    private static char valueToChar(final byte b) {
        if (b < 26) {
            return (char) (b + 65);
        }

        if (b < 32) {
            return (char) (b + 24);
        }

        throw new IllegalArgumentException("Byte is not a value Base32 value.");
    }
}
