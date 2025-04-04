/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.insights;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.model.Status;

import java.lang.reflect.Method;


/**
 * A utility class for managing Allure test reporting functionalities.
 * This class provides methods to update Allure reports with test steps, 
 * pass/fail statuses, and test descriptions.
 * 
 * The class contains helper methods to:
 * - Add conditional steps with pass/fail messages
 * - Mark steps as passed with custom messages
 * - Mark steps as failed with custom messages
 * - Retrieve test descriptions from @Description annotations
 *
 * This class integrates with the Allure reporting framework to provide
 * detailed test execution reports.
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class UpdateAllure {

    /**
     * Adds a step to the Allure report with a conditional pass/fail status.
     *
     * @param result   The result of the test step (true for pass, false for fail).
     * @param passMsg  The message to log if the step passes.
     * @param failMsg  The message to log if the step fails.
     */
    public static void step(boolean result, String passMsg, String failMsg) {
        if (result == true) {
            pass(passMsg);
        } else {
            fail(failMsg);
        }
    }

    /**
     * Adds a step to the Allure report with a custom message and status.
     *
     * @param parameter The message to log in the Allure report.
     */
    public static void pass(final String parameter) {
        Allure.step(parameter, Status.PASSED);
    }

    /**
     * Adds a step to the Allure report with a custom message and status.
     *
     * @param parameter The message to log in the Allure report.
     */
    public static void fail(final String parameter) {
        Allure.step(parameter, Status.FAILED);
    }

    /**
     * Retrieves the test description from the method annotated with @Description.
     *
     * @param methodName The name of the test method to retrieve the description for.
     * @return The description of the test method, or a default message if not found.
     */
    public String getTestDescription(String methodName) {
        try {
            Method method = this.getClass().getMethod(methodName);
            Description description = method.getAnnotation(Description.class);
            return (description != null) ? description.value() : "No description found";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return "Error fetching description";
        }
    }
}
