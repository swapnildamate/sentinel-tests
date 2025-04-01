package org.sentinel.tests.common;

import org.sentinel.tests.utils.AllureLogUtil;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LogAssert extends SoftAssert {
    private static final Logger LOGGER = LoggerUtil.getLogger();
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
            AllureLogUtil.pass(passMsg);
        } else {
            LOGGER.severe(failMsg);
            AllureLogUtil.fail(failMsg);
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