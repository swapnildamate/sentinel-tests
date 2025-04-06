package org.sentinel.tests.utils.testng;

import org.sentinel.tests.constants.ConfigConstants;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * A class that implements TestNG's IRetryAnalyzer interface to provide test retry functionality.
 * This analyzer will retry failed test cases up to a maximum number of times specified in the TestNG parameters.
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.testng.IRetryAnalyzer
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    /**
     * Counter for the current number of retry attempts.
     */
    private int retryCount = 0;

    /**
     * Maximum number of retry attempts allowed, read from TestNG parameters.
     */
    private static final int MAX_RETRY_COUNT = Integer.parseInt(ReadTestNG.getParameter(ConfigConstants.MAX_RETRY_COUNT));

    /**
     * Method called by TestNG to determine if a failed test should be retried.
     *
     * @param result The result of the test case that just ran
     * @return true if the test should be retried, false otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            LoggerUtil.info("Retrying " + result.getName() + " (" + retryCount + "/" + MAX_RETRY_COUNT + ")");
            return true;
        }
        return false;
    }
}
