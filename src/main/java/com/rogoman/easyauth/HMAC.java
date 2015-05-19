package com.rogoman.easyauth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

/**
 * A wrapper class for calling the HMAC providers.
 */
final class HMAC {

    /**
     * Private constructor to prevent from instantiating the class.
     */
    private HMAC() {
    }

    /**
     * Calculates the HMAC digest value based on the provided parameters.
     *
     * @param msg       Message
     * @param keyString Key to be used in the hashing process
     * @param algorithm HMAC algorithm to be used
     * @return HMAC digest
     * @exception java.io.UnsupportedEncodingException if UTF-8 or ASCII encoding is not available
     * @exception java.security.NoSuchAlgorithmException thrown when the passed digest algorithm name cannot be recognized
     * @exception java.security.InvalidKeyException thrown when the passed secret key value is invalid according to the digest algorithm
     */
    static String hmacDigest(final String msg, final String keyString, final String algorithm)
            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        if (msg == null) {
            throw new IllegalArgumentException("msg is empty");
        }
        if (keyString == null) {
            throw new IllegalArgumentException("keyString is empty");
        }
        if (StringUtils.isEmpty(algorithm)) {
            throw new IllegalArgumentException("algo is empty");
        }

        String digest = null;
        byte[] keyAsBytes = (keyString).getBytes("UTF-8");
        byte[] msgAsBytes = msg.getBytes("ASCII");

        byte[] byteResult = hmacDigest(msgAsBytes, keyAsBytes, algorithm);
        digest = convertToHexString(byteResult);
        return digest;
    }

    /**
     * Calculates the HMAC digest value based on the provided parameters.
     *
     * @param msg       Message
     * @param secretKey Key to be used in the hashing process
     * @param algorithm HMAC algorithm to be used
     * @return HMAC digest
     * @exception java.security.NoSuchAlgorithmException thrown when the passed digest algorithm name cannot be recognized
     * @exception java.security.InvalidKeyException thrown when the passed secret key value is invalid according to the digest algorithm
     */
    static byte[] hmacDigest(final byte[] msg, final byte[] secretKey, final String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        if (msg == null) {
            throw new IllegalArgumentException("msg is empty");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey is empty");
        }
        if (StringUtils.isEmpty(algorithm)) {
            throw new IllegalArgumentException("algo is empty");
        }

        SecretKeySpec key = new SecretKeySpec(secretKey, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(key);
        return mac.doFinal(msg);
    }

    /**
     * Converts a byte array to a hex string.
     *
     * @param byteArray byte array
     * @return hex string
     */
    static String convertToHexString(final byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("byteArray is null");
        }

        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            String hex = Integer.toHexString(0xFF & byteArray[i]);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        return hash.toString();
    }
}
