/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.config.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sentinel.tests.constants.ConfigConstants;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.testng.ReadTestNG;

/**
 * Manages WebDriver instances in a thread-safe manner for browser automation.
 * This class provides functionality to create and manage WebDriver sessions
 * for both local and remote browser instances using ThreadLocal storage.
 * 
 * The class implements the Singleton pattern with double-checked locking
 * to ensure thread safety when creating WebDriver instances.
 * 
 * Key features:
 * - Thread-safe WebDriver management
 * - Support for both local and remote browser sessions
 * - Automatic resource cleanup
 * - Session tracking for remote executions
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class WebDriverManager {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static String sessionId=null;

    private WebDriverManager(){

    }
   
    /**
     * Gets or creates a WebDriver instance using thread-local storage to ensure thread safety.
     * Uses double-checked locking pattern to prevent race conditions during driver initialization.
     * The driver type (remote or local) is determined by the "run_on" parameter from TestNG configuration.
     *
     * @return WebDriver instance for the current thread
     * @throws IllegalArgumentException if the run_on parameter has an unsupported value
     */
    public static WebDriver getDriverInstance() {
        if (driverThreadLocal.get() == null) {
            synchronized (WebDriverManager.class) { // Prevent race conditions
                if (driverThreadLocal.get() == null) {// Double-check locking
                    String runOn = ReadTestNG.getParameter(ConfigConstants.RUN_ON);
                    LoggerUtil.info(String.format("Doing setup for %s runs.", runOn));
                    switch (runOn.toLowerCase()) {
                        case "remote":
                            LoggerUtil.info("Launching Remote Instance.");
                            driverThreadLocal.set(BrowserManager.getRemoteBrowser());
                            sessionId = ((RemoteWebDriver) driverThreadLocal.get()).getSessionId().toString();
                            LoggerUtil.info(String.format("Session Id: %s", sessionId));
                            break;
                        case "local":
                            LoggerUtil.info("Launching Local Instance.");
                            driverThreadLocal.set(BrowserManager.getLocalBrowser());
                            break;
                        default:
                            LoggerUtil.warning(String.format("Unsupported run params: %s", runOn));
                            throw new IllegalArgumentException("Unsupported params: " + runOn);
                    }
                }
            }
        }
        return driverThreadLocal.get();
    }

    /**
     * Quits the WebDriver instance associated with the current thread and performs cleanup.
     * This method ensures proper closure of browser sessions and prevents memory leaks
     * by removing the thread-local reference.
     * 
     * The method performs the following operations:
     * 1. Retrieves the WebDriver instance from ThreadLocal storage
     * 2. Quits the browser session if a driver instance exists
     * 3. Removes the ThreadLocal reference to prevent memory leaks
     * 
     * If an exception occurs during driver quit operation, it will be logged
     * and the ThreadLocal cleanup will still be performed in the finally block.
     * 
     * @see ThreadLocal
     * @see WebDriver#quit()
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                LoggerUtil.info(String.format("Thread %s - WebDriver Closed.", Thread.currentThread().getId()));
            } catch (Exception e) {
                LoggerUtil.error("Error while quitting WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove(); // Clean up to prevent memory leaks
            }
        }
    }

    public static String getSessionId(){
        return sessionId;
    }
}