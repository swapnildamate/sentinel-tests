package org.sentinel.tests.enums;

public enum BrowserType {
    CHROME,
    FIREFOX,
    SAFARI,
    EDGE;

    public static String getBrowserType(String browserType) {
        return BrowserType.valueOf(browserType.toUpperCase()).toString();
    }

}
