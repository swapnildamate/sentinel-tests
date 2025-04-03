package org.sentinel.tests.utils.testng;

import org.sentinel.tests.utils.log.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 1; // Number of retries

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LoggerUtil.info("Retrying " + result.getName() + " (" + retryCount + "/" + maxRetryCount + ")");
            return true;
        }
        return false;
    }
}
