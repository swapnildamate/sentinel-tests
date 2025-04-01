package org.sentinel.tests.ui.common;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.common.LoggerUtil;

;

public class WebDriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static synchronized WebDriver getDriver(String osType, String browser) {
        if (driverThreadLocal.get() == null) {
            LoggerUtil.info(String.format("Test initialized with OS: %s, Browser: %s", osType, browser));
            driverThreadLocal.set(LocalDriver.getDriver(browser));
        }
        return driverThreadLocal.get();
    }

    public static synchronized void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            LoggerUtil.info("Driver Closed.");
        }
    }

}