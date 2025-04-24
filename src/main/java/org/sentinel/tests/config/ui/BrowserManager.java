/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.config.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.testng.ReadTestNG;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Manages browser initialization and configuration for both local and remote WebDriver instances.
 * This class provides utility methods to create and configure different browser sessions for web testing.
 *
 * <p>Supported browsers for local testing include:
 * <ul>
 *   <li>Chrome - with disabled web security and remote origin permissions
 *   <li>Firefox - with default configuration
 *   <li>Edge - with default configuration
 * </ul>
 *
 * <p>Remote testing is supported through LambdaTest integration using environment credentials.
 *
 * <p>The class uses WebDriverManager for automatic driver binary management and configuration.
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.openqa.selenium.WebDriver
 * @see io.github.bonigarcia.wdm.WebDriverManager
 * @see org.openqa.selenium.remote.RemoteWebDriver
 */
public class BrowserManager {

    private BrowserManager(){

    }
    /**
     * Creates and initializes a WebDriver instance based on the specified browser in TestNG parameters.
     * Supports Chrome, Firefox, and Edge browsers with specific configurations.
     *
     * <p>For Chrome: Disables web security, allows insecure content, and remote origins
     * <p>For Firefox and Edge: Uses default configurations
     *
     * @return WebDriver - Initialized WebDriver instance for the specified browser
     * @throws IllegalArgumentException if an unsupported browser is specified
     * @throws RuntimeException if WebDriver initialization fails for any reason
     * @see WebDriver
     * @see ChromeDriver
     * @see FirefoxDriver
     * @see EdgeDriver
     */
    public static WebDriver getLocalBrowser() {
        WebDriver driver;
        String browser = ReadTestNG.getParameter("browserName");
        try {
            LoggerUtil.info("Initializing browser: " + browser);
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-web-security");
                    chromeOptions.addArguments("--allow-running-insecure-content");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driver = new EdgeDriver(edgeOptions);
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            LoggerUtil.info("Browser initialized successfully: " + browser);
            return driver;
        } catch (Exception e) {
            LoggerUtil.error("Failed to initialize browser: " + browser + " - Error: " + e.getMessage());
            throw new IllegalArgumentException("WebDriver initialization failed", e);
        }
    }

    /**
     * Creates and returns a RemoteWebDriver instance for remote browser testing.
     * 
     * This method configures and initializes a remote WebDriver session using LambdaTest capabilities
     * and credentials. It uses environment variables for authentication.
     *
     * @return WebDriver instance configured for remote testing
     * @throws RuntimeException if the LambdaTest URL is malformed or connection cannot be established
     */
    public static WebDriver getRemoteBrowser() {
        try {
            DesiredCapabilities capabilities = Capabilities.getCapabilities();
            LoggerUtil.info("Remote browser opening with : %s" + capabilities);
            String ltUrl = String.format(ReadTestNG.getParameter("lt_URL"), System.getenv("LT_USER"), System.getenv("LT_ACCESS_KEY"));
            return new RemoteWebDriver(new URL(ltUrl), capabilities);
        } catch (MalformedURLException e) {
            LoggerUtil.info("Remote browser not launched");
            throw new IllegalArgumentException("Remote browser not launched");
        }
    }
}