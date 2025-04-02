package org.sentinel.tests.utils.testng;

import org.sentinel.tests.utils.insights.UpdateAllure;
import org.sentinel.tests.utils.log.Logger;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class AssertLog extends SoftAssert {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger();
    private final List<String> assertionErrors = new ArrayList<>();

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String message;
        if (assertCommand != null) {
            message = "Assertion Failed: Expected [" + assertCommand.getExpected() + "] but found [" + assertCommand.getActual() + "]";
        } else {
            message = "Assertion Failed: " + ex.getMessage(); // Handle custom failures
        }
        //LOGGER.severe(message);
        assertionErrors.add(message);
    }


    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String message = "Assertion Passed: Expected [" + assertCommand.getExpected() + "] and found [" + assertCommand.getActual() + "]";
        LOGGER.info(message);
    }

    public void assertTrue(boolean condition, String passMsg, String failMsg) {
        if (condition) {
            LOGGER.info(passMsg);
        } else {
            LOGGER.severe(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertString(String actual, String expected, String passMsg, String failMsg) {
        if (actual != null && actual.equalsIgnoreCase(expected)) {
            LOGGER.info(passMsg);
        } else {
            String failureMessage = failMsg + " Expected: [" + expected + "] but found: [" + actual + "]";
            LOGGER.severe(failureMessage);
            onAssertFailure(null, new AssertionError(failureMessage));
        }
    }


    public void assertFalse(boolean condition, String passMsg, String failMsg) {
        if (!condition) {
            LOGGER.info(passMsg);
        } else {
            LOGGER.severe(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertEquals(Object actual, Object expected, String passMsg, String failMsg) {
        if (actual == null ? expected == null : actual.equals(expected)) {
            LOGGER.info(passMsg);
            UpdateAllure.pass(passMsg);
        } else {
            LOGGER.severe(failMsg);
            UpdateAllure.fail(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertNotEquals(Object actual, Object expected, String passMsg, String failMsg) {
        if (actual == null ? expected != null : !actual.equals(expected)) {
            LOGGER.info(passMsg);
        } else {
            LOGGER.severe(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertNull(Object object, String passMsg, String failMsg) {
        if (object == null) {
            LOGGER.info(passMsg);
        } else {
            LOGGER.severe(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertNotNull(Object object, String passMsg, String failMsg) {
        if (object != null) {
            LOGGER.info(passMsg);
        } else {
            LOGGER.severe(failMsg);
            onAssertFailure(null, new AssertionError(failMsg));
        }
    }

    public void assertAllWithLog() {
        if (!assertionErrors.isEmpty()) {
            LOGGER.severe("Test failed with " + assertionErrors.size() + " assertion errors.");
            LOGGER.severe("Check [SEVERE] log.");
            assertionErrors.forEach(LOGGER::severe);
            // Clear assertion errors before throwing the exception
            List<String> errorsCopy = new ArrayList<>(assertionErrors);
            assertionErrors.clear();
            throw new AssertionError("Test failed with multiple assertion errors. Check logs for details.\n" + errorsCopy);
        }
        // Ensure errors are cleared for the next test
        assertionErrors.clear();
    }



}