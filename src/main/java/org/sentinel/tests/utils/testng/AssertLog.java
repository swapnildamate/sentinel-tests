
/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.testng;

import org.sentinel.tests.constants.ConfigConstants;
import org.sentinel.tests.utils.insights.AllureUtil;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom extension of TestNG's SoftAssert class that provides enhanced
 * assertion functionality with logging capabilities.
 * This class allows for multiple assertions to be performed while collecting
 * all failures before reporting them.
 * It includes custom assertion methods with detailed logging and integration
 * with Allure reporting.
 * 
 * Key features:
 * - Maintains a list of assertion errors for comprehensive failure reporting
 * - Provides detailed logging for both successful and failed assertions
 * - Integrates with LoggerUtil for consistent logging
 * - Supports Allure reporting updates
 * - Includes custom assertion methods for common validation scenarios
 * 
 * Usage example:
 * 
 * <pre>
 * AssertLog softAssert = new AssertLog();
 * softAssert.assertTrue(condition, "Pass message", "Fail message");
 * softAssert.assertEquals(actual, expected, "Pass message", "Fail message");
 * softAssert.assertAllWithLog(); // Throws assertion error if any assertions failed
 * </pre>
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.testng.asserts.SoftAssert
 * @see IAssert
 */
public class AssertLog extends SoftAssert {
    private final List<String> assertionErrors = new ArrayList<>();

    /**
     * Handles assertion failures by capturing the failure details and storing them
     * for later reference.
     * This method is called automatically when a TestNG assertion fails during test
     * execution.
     *
     * @param assertCommand The IAssert object containing details about the failed
     *                      assertion. May be null for custom failures.
     * @param ex            The AssertionError that was thrown when the assertion
     *                      failed
     */
    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String message;
        if (assertCommand != null) {
            message = "Assertion Failed: Expected <" + assertCommand.getExpected() + "> but found <"
                    + assertCommand.getActual() + ">";
        } else {
            message = "Assertion Failed: " + ex.getMessage(); // Handle custom failures
        }
        assertionErrors.add(message);
    }

    /**
     * Handles assertion success by logging the success message.
     * This method is called automatically when a TestNG assertion passes during
     * test execution.
     *
     * @param assertCommand The IAssert object containing details about the passed
     *                      assertion
     */
    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String message = "Assertion Passed: Expected <" + assertCommand.getExpected() + "> and found <"
                + assertCommand.getActual() + ">";
        LoggerUtil.info(message);
    }

    /**
     * Asserts that the given condition is true. If the condition is false, logs the
     * failure message and throws an AssertionError.
     *
     * @param condition The condition to check
     * @param passMsg   The message to log if the assertion passes
     * @param failMsg   The message to log if the assertion fails
     */
    public void assertTrue(boolean condition, String passMsg, String failMsg) {
        if (condition) {
            LoggerUtil.info(passMsg);
        } else {
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    /**
     * Asserts that the given string is equal to the expected string, ignoring case.
     * If not, logs the failure message and throws an AssertionError.
     *
     * @param actual   The actual string to check
     * @param expected The expected string to compare against
     * @param passMsg  The message to log if the assertion passes
     * @param failMsg  The message to log if the assertion fails
     */
    public void assertString(String actual, String expected, String passMsg, String failMsg) {
        if (actual != null && actual.equalsIgnoreCase(expected)) {
            LoggerUtil.info(passMsg);
        } else {
            String failureMessage = failMsg + " Expected: <" + expected + "> but found: <" + actual + ">";
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failureMessage));
        }
    }

    /**
     * Asserts that the given condition is false. If the condition is true, logs the
     * failure message and throws an AssertionError.
     *
     * @param condition The condition to check
     * @param passMsg   The message to log if the assertion passes
     * @param failMsg   The message to log if the assertion fails
     */
    public void assertFalse(boolean condition, String passMsg, String failMsg) {
        if (!condition) {
            LoggerUtil.info(passMsg);
        } else {
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    /**
     * Asserts that the given object is equal to the expected object. If not, logs
     * the failure message and throws an AssertionError.
     *
     * @param actual   The actual object to check
     * @param expected The expected object to compare against
     * @param passMsg  The message to log if the assertion passes
     * @param failMsg  The message to log if the assertion fails
     */
    public void assertEquals(Object actual, Object expected, String passMsg, String failMsg) {
        if (actual == null ? expected == null : actual.equals(expected)) {
            LoggerUtil.info(passMsg);
        } else {
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    /**
     * Asserts that the given object is not equal to the expected object. If they
     * are equal, logs the failure message and throws an AssertionError.
     *
     * @param actual   The actual object to check
     * @param expected The expected object to compare against
     * @param passMsg  The message to log if the assertion passes
     * @param failMsg  The message to log if the assertion fails
     */
    public void assertNotEquals(Object actual, Object expected, String passMsg, String failMsg) {
        if (actual == null ? expected != null : !actual.equals(expected)) {
            LoggerUtil.info(passMsg);
        } else {
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    /**
     * Asserts that the given object is null. If not, logs the failure message and
     * throws an AssertionError.
     *
     * @param object  The object to check
     * @param passMsg The message to log if the assertion passes
     * @param failMsg The message to log if the assertion fails
     */
    public void assertNull(Object object, String passMsg, String failMsg) {
        if (object == null) {
            LoggerUtil.info(passMsg);
        } else {
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    /**
     * Asserts that the given object is not null. If it is, logs the failure message
     * and throws an AssertionError.
     *
     * @param object  The object to check
     * @param passMsg The message to log if the assertion passes
     * @param failMsg The message to log if the assertion fails
     */
    public void assertNotNull(Object object, String passMsg, String failMsg) {
        if (object != null) {
            LoggerUtil.info(passMsg);
        } else {
            AllureUtil.fail(failMsg,Boolean.valueOf(ReadTestNG.getParameter(ConfigConstants.TAKE_SNAP_ON_FAILURE)));
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    /**
     * Asserts that all collected assertion errors are logged and throws an
     * AssertionError if any exist.
     * This method should be called at the end of a test to ensure all assertions
     * are checked.
     *
     * @throws AssertionError if there are any assertion errors collected during the
     *                        test
     */
    public void assertAllWithLog() {
        if (!assertionErrors.isEmpty()) {
            LoggerUtil.error("Test failed with " + assertionErrors.size() + " assertion errors.");
            LoggerUtil.error("Check [SEVERE] log.");
            assertionErrors.forEach(LoggerUtil::error);
            // Clear assertion errors before throwing the exception
            List<String> errorsCopy = new ArrayList<>(assertionErrors);
            assertionErrors.clear();
            throw new AssertionError(
                    "Test failed with multiple assertion errors. Check logs for details. " + errorsCopy);
        }
        // Ensure errors are cleared for the next test
        assertionErrors.clear();
    }

}