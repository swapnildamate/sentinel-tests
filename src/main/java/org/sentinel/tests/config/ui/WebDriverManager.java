package org.sentinel.tests.config.ui;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.utils.log.Logger;

public class WebDriverManager {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver(String osType, String browser) {
        if (driverThreadLocal.get() == null) {
            synchronized (WebDriverManager.class) { // Prevent race conditions
                if (driverThreadLocal.get() == null) { // Double-check locking
                    Logger.info(String.format("Thread %s - Initializing WebDriver for OS: %s, Browser: %s",
                            Thread.currentThread().getId(), osType, browser));
                    driverThreadLocal.set(BrowserManager.getBrowser(browser));
                }
            }
        }
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                Logger.info(String.format("Thread %s - WebDriver Closed.", Thread.currentThread().getId()));
            } catch (Exception e) {
                Logger.error("Error while quitting WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove(); // Clean up to prevent memory leaks
            }
        }
    }
}