/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils;

import org.sentinel.tests.utils.log.LoggerUtil;

import java.util.Base64;

/**
 * A utility class for encoding and decoding strings using Base64.
 * This class provides methods to encode a string into Base64 format and decode a Base64 string back to its original form.
 * It also includes a main method for testing the encoding and decoding functionality.
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class EncoderDecoder {
    public static void main(String[] args) {
        String originalText = "";
        LoggerUtil.info("Encoded: " + encode(originalText));
        LoggerUtil.info("Decoded: " + decode(encode(originalText)));
    }

    /**
     * Encodes the given string into Base64 format.
     *
     * @param string The string to be encoded
     * @return The Base64 encoded string
     */
    public static String encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }


    /**
     * Decodes a Base64 encoded string into its original form.
     * 
     * @param encodedText the Base64 encoded string to be decoded
     * @return the decoded string in its original form
     * @throws IllegalArgumentException if the input string is not a valid Base64 encoded text
     */
    public static String decode(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText));
    }
}
