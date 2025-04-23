/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.config.ui;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.sentinel.tests.base.BaseUIService;
import org.sentinel.tests.constants.Config;
import org.sentinel.tests.utils.testng.ReadTestNG;

/**
 * Utility class for managing Selenium WebDriver capabilities configuration.
 * <p>
 * This class provides functionality to create and configure browser capabilities
 * for Selenium WebDriver tests. It handles setting up platform, browser name,
 * version and other test environment parameters.
 * <p>
 * The capabilities configuration is based on parameters read from TestNG
 * configuration, allowing flexible test environment setup.
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.openqa.selenium.remote.DesiredCapabilities
 */
public class Capabilities {

    private Capabilities() {

    }

    /**
     * Creates and configures DesiredCapabilities object with test parameters.
     * <p>
     * This method sets up browser capabilities by reading test configuration parameters:
     * - platform: The operating system platform
     * - browserName: The name of the browser to use
     * - version: Browser version
     * <p>
     * The capabilities are used to configure test environment settings for browser automation.
     *
     * @return DesiredCapabilities object configured with test parameters
     * @see org.openqa.selenium.remote.DesiredCapabilities
     */
    public static DesiredCapabilities getCapabilities() {
        // Set up the desired capabilities for each iteration
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(Config.PLATFORM, ReadTestNG.getParameter(Config.PLATFORM));
        capabilities.setCapability(Config.BROWSER_NAME, ReadTestNG.getParameter(Config.BROWSER_NAME));
        capabilities.setCapability(Config.BROWSER_VERSION, ReadTestNG.getParameter(Config.BROWSER_VERSION));
        capabilities.setCapability(Config.BUILD_NAME,System.getenv(Config.LT_BUILD));
        capabilities.setCapability(Config.RUN_NAME, BaseUIService.getTestName());
        return capabilities;
    }
}
