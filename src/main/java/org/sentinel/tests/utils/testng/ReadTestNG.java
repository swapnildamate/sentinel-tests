/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.testng;

import org.sentinel.tests.utils.log.LoggerUtil;
import org.testng.ITestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing TestNG test parameters.
 * This class provides functionality to load, store, and retrieve test parameters
 * from TestNG XML configuration files. It maintains a static map of parameters
 * that can be accessed throughout the test execution.
 *
 * <p>The class offers methods to:
 * <ul>
 *   <li>Load parameters from TestNG context</li>
 *   <li>Retrieve individual parameters by key</li>
 *   <li>Get all stored parameters</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>
 * {@code
 * @Test
 * public void testMethod(ITestContext context) {
 *     ReadTestNG.loadParameters(context);
 *     String param = ReadTestNG.getParameter("paramKey");
 * }
 * }
 * </pre>
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class ReadTestNG {

    private static final Map<String, String> paramMap = new HashMap<>();

    /**
     * Loads parameters from the TestNG context into a static map.
     * This method is called at the start of the test execution.
     *
     * @param context The test context containing information about the test run
     */
    public static void loadParameters(ITestContext context) {
        LoggerUtil.info("Parameter Loading.....");
        context.getCurrentXmlTest().getAllParameters()
                .forEach(paramMap::put);
        LoggerUtil.info("Parameter Completed.....");
    }

    /**
     * Retrieves a parameter value from the static map using the specified key.
     *
     * @param key The key for the parameter to retrieve
     * @return The value of the parameter, or null if not found
     */
    public static String getParameter(String key) {
        return paramMap.get(key);
    }

    /**
     * Retrieves all parameters from the static map.
     *
     * @return A map containing all parameters and their values
     */
    public static Map<String, String> getAllParameters() {
        return paramMap;
    }
}
