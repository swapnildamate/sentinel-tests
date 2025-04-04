/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.insights;

import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.testng.ReadTestNG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for setting up environment properties for Allure reporting.
 * This class handles the creation and population of environment.properties file
 * which is used by Allure to display test environment information in the reports.
 * 
 * The class captures various execution environment details including:
 * - Execution platform
 * - Browser information
 * - Testing URL
 * - User executing the tests
 * - Browser version
 * - Execution platform (run_on parameter)
 * 
 * The environment properties file is created in the 'reports/allure-results' 
 * directory relative to the project root.
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see Properties
 * @see FileWriter
 * @see LoggerUtil
 */
public class AllureEnvironmentSetup {
   public static Properties properties;

    /**
     * Creates an environment.properties file for Allure reporting with system and test configuration details.
     * The file is created in the 'reports/allure-results' directory under the project root.
     * 
     * The following properties are included in the file:
     * - Executed On: The platform where tests are run (from TestNG parameters)
     * - Platform: Operating system name
     * - Browser: Browser used for testing (from TestNG parameters)
     * - Version: Test version (from TestNG parameters)
     * - URL: Application URL (from TestNG parameters)
     * - Executed By: Username of the person executing the tests
     * 
     * @throws IOException If there are issues creating the file or writing properties
     */
    public static void createEnvironmentFile() {
        try {
            properties = new Properties();
            properties.setProperty("Executed On", ReadTestNG.getParameter("run_on"));
            properties.setProperty("Platform", System.getProperty("os.name"));
            properties.setProperty("Browser", ReadTestNG.getParameter("browserName"));
            properties.setProperty("Version", ReadTestNG.getParameter("version"));
            properties.setProperty("URL", ReadTestNG.getParameter("appURL"));
            properties.setProperty("Executed By", System.getProperty("user.name"));

            // Get project root directory dynamically
            String projectRoot = System.getProperty("user.dir");
            File allureResultsDir = new File(projectRoot, "reports/allure-results");
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs(); // Create directory if it doesn't exist
            }

            // Create the environment.properties file dynamically
            File envFile = new File(allureResultsDir, "environment.properties");
            FileWriter writer = new FileWriter(envFile);
            properties.store(writer, "Allure Environment Properties");
            writer.close();

            LoggerUtil.info("Environment Properties added for Allure Report.");
        } catch (IOException e) {
            LoggerUtil.error("Un able to Environment Properties added for Allure Report.");
            e.printStackTrace();
        }
    }

}