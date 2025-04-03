package org.sentinel.tests.utils.testng;

import org.sentinel.tests.utils.insights.UpdateAllure;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class AssertLog extends SoftAssert {
    private final List<String> assertionErrors = new ArrayList<>();

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String message;
        if (assertCommand != null) {
            message = "Assertion Failed: Expected <" + assertCommand.getExpected() + "> but found <" + assertCommand.getActual() + ">";
        } else {
            message = "Assertion Failed: " + ex.getMessage(); // Handle custom failures
        }
        assertionErrors.add(message);
    }


    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String message = "Assertion Passed: Expected <" + assertCommand.getExpected() + "> and found <" + assertCommand.getActual() + ">";
        LoggerUtil.info(message);
    }

    public void assertTrue(boolean condition, String passMsg, String failMsg) {
        if (condition) {
            LoggerUtil.info(passMsg);
        } else {
            LoggerUtil.error(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertString(String actual, String expected, String passMsg, String failMsg) {
        if (actual != null && actual.equalsIgnoreCase(expected)) {
            LoggerUtil.info(passMsg);
        } else {
            String failureMessage = failMsg + " Expected: <" + expected + "> but found: <" + actual + ">";
            LoggerUtil.error(failureMessage);
            onAssertFailure(null, new AssertionError(failureMessage));
        }
    }


    public void assertFalse(boolean condition, String passMsg, String failMsg) {
        if (!condition) {
            LoggerUtil.info(passMsg);
        } else {
            LoggerUtil.error(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertEquals(Object actual, Object expected, String passMsg, String failMsg) {
        if (actual == null ? expected == null : actual.equals(expected)) {
            LoggerUtil.info(passMsg);
            UpdateAllure.pass(passMsg);
        } else {
            LoggerUtil.error(failMsg);
            UpdateAllure.fail(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertNotEquals(Object actual, Object expected, String passMsg, String failMsg) {
        if (actual == null ? expected != null : !actual.equals(expected)) {
            LoggerUtil.info(passMsg);
        } else {
            LoggerUtil.error(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertNull(Object object, String passMsg, String failMsg) {
        if (object == null) {
            LoggerUtil.info(passMsg);
        } else {
            LoggerUtil.error(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertNotNull(Object object, String passMsg, String failMsg) {
        if (object != null) {
            LoggerUtil.info(passMsg);
        } else {
            LoggerUtil.error(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertAllWithLog() {
        if (!assertionErrors.isEmpty()) {
            LoggerUtil.error("Test failed with " + assertionErrors.size() + " assertion errors.");
            LoggerUtil.error("Check [SEVERE] log.");
            assertionErrors.forEach(LoggerUtil::error);
            // Clear assertion errors before throwing the exception
            List<String> errorsCopy = new ArrayList<>(assertionErrors);
            assertionErrors.clear();
            throw new AssertionError("Test failed with multiple assertion errors. Check logs for details. " + errorsCopy);
        }
        // Ensure errors are cleared for the next test
        assertionErrors.clear();
    }

}