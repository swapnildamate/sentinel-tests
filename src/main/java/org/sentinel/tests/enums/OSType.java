/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.enums;

/**
 * Enum representing different operating system types.
 * Provides functionality to handle and identify different OS platforms.
 * Currently supports Windows, Mac, and Linux operating systems.
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public enum OSType {
    WINDOWS,
    MAC,
    LINUX;

    /**
     * Converts the provided OS type string to its uppercase equivalent and returns it as a string representation.
     *
     * @param getOSType The operating system type string to be converted
     * @return The uppercase string representation of the OS type
     * @throws IllegalArgumentException if the provided string doesn't match any defined OS type
     */
    public static String getOSType(String getOSType) {
        return OSType.valueOf(getOSType.toUpperCase()).toString();
    }
}
