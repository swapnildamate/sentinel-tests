/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.enums;

/**
 * Enum representing different types of web browsers supported by the application.
 * The enum provides a utility method to convert string browser names to their corresponding enum values.
 * 
 * Supported browser types include:
 * <ul>
 *     <li>CHROME - Google Chrome browser</li>
 *     <li>FIREFOX - Mozilla Firefox browser</li>
 *     <li>SAFARI - Apple Safari browser</li>
 *     <li>EDGE - Microsoft Edge browser</li>
 * </ul>
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    SAFARI,
    EDGE;

    /**
     * Retrieves the browser type string representation from the given browser type name.
     *
     * @param browserType the name of the browser type (case-insensitive)
     * @return the string representation of the browser type in uppercase
     * @throws IllegalArgumentException if the provided browser type does not match any enum constant
     */
    public static String getBrowserType(String browserType) {
        return BrowserType.valueOf(browserType.toUpperCase()).toString();
    }

}
