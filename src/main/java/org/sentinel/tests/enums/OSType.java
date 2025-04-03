package org.sentinel.tests.enums;

public enum OSType {
    WINDOWS,
    MAC,
    LINUX;

    public static String getOSType(String getOSType) {
        return OSType.valueOf(getOSType.toUpperCase()).toString();
    }
}
