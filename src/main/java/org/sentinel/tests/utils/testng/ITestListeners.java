/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.testng;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.constants.ConfigConstants;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.insights.CaptureAttachment;
import org.sentinel.tests.utils.ExcelUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.*;


/**
 * A TestNG listener implementation that provides test execution monitoring and reporting functionality.
 * This class implements ITestListener to hook into the TestNG test lifecycle events.
 * <p>
 * Key features:
 * - Logs test execution status at various stages
 * - Captures screenshots on test failures when WebDriver is available
 * - Maintains test results in a thread-safe collection
 * - Exports test results to Excel at the end of test execution
 * <p>
 * The listener tracks the following test events:
 * - Test start/finish
 * - Individual test method start
 * - Test success
 * - Test failure (with screenshot capture)
 * - Test skipped
 * <p>
 * Results are stored with the following information:
 * - Package name
 * - Method name
 * - Execution status
 * - Remarks/error messages
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.testng.ITestListener
 * @see org.openqa.selenium.WebDriver
 */
public class ITestListeners implements ITestListener {
    protected List<Map<String, String>> testCasesResultMap;


    /**
     * Called when a test class starts its execution.
     * Logs the start of test execution using LoggerUtil.
     *
     * @param context The test context containing information about the test run
     * @see org.testng.ITestListener#onStart(org.testng.ITestContext)
     */
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        LoggerUtil.info("********** Test Execution Started.....**********");
    }

    /**
     * Called when a test class finishes its execution.
     * Logs the end of test execution using LoggerUtil.
     *
     * @param result The test context containing information about the test run
     * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtil.info("********** Test Started.....**********");
    }

    /**
     * Called when a test method finishes its execution.
     * Logs the end of test method execution using LoggerUtil.
     *
     * @param result The result of the test method execution
     * @see org.testng.ITestListener#onTestSuccess(org.testng.ITestResult)
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        addTestResult(result, "Pass", "Test executed successfully");
        LoggerUtil.info("********** Test Passed. **********");
    }

    /**
     * Called when a test method fails.
     * Captures a screenshot if WebDriver is available and logs the failure using LoggerUtil.
     *
     * @param result The result of the test method execution
     * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
     */
    @Override
    public void onTestFailure(ITestResult result) {

        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver) context.getAttribute("driver");
        if (driver != null) {
            CaptureAttachment.captureScreenshot(driver);
            LoggerUtil.info("Screenshot not captured.");// Capture screenshot
        } else {
            LoggerUtil.warning("Driver is null. Screenshot not captured.");
        }
        addTestResult(result, "Fail", result.getThrowable() != null ? result.getThrowable().getMessage() : "No error message");
        LoggerUtil.info("********** Test Fail. **********");
    }

    /**
     * Called when a test method is skipped.
     * Logs the skip status using LoggerUtil.
     *
     * @param result The result of the test method execution
     * @see org.testng.ITestListener#onTestSkipped(org.testng.ITestResult)
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        addTestResult(result, "Skip", "Test was skipped");
    }

    /**
     * Called when a test class finishes its execution.
     * Exports the test results to Excel and logs the completion using LoggerUtil.
     *
     * @param context The test context containing information about the test run
     * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onFinish(ITestContext context) {
        testCasesResultMap = (List<Map<String, String>>) context.getAttribute(ConfigConstants.TEST_CASES_RESULT_MAP);
        if (!testCasesResultMap.isEmpty()) {
            ExcelUtil.addTestCases(testCasesResultMap);
        }
        LoggerUtil.info("********** Test Execution Completed.....**********");
    }

    /**
     * Adds a test result to the shared list of test results.
     * This method is synchronized to ensure thread safety when adding results.
     *
     * @param result The result of the test method execution
     * @param status The status of the test (e.g., "Pass", "Fail", "Skip")
     * @param remark Additional remarks or error messages related to the test result
     */
    private void addTestResult(ITestResult result, String status, String remark) {
        ITestContext context = result.getTestContext();

        // Retrieve or initialize testCasesResultMap
        testCasesResultMap = (List<Map<String, String>>) context.getAttribute("testCasesResultMap");

        if (testCasesResultMap == null) {
            testCasesResultMap = Collections.synchronizedList(new ArrayList<>()); // Ensures thread safety
            context.setAttribute("testCasesResultMap", testCasesResultMap);
        }

        String fullClassName = result.getTestClass().getName();
        int lastDotIndex = fullClassName.lastIndexOf('.');

        String packageName = "Adhoc"; // Default if no package is found
        if (lastDotIndex != -1) {
            packageName = fullClassName.substring(0, lastDotIndex);
            LoggerUtil.info("Package Name: " + packageName);
        } else {
            LoggerUtil.info("No package found. Class might be in the default package.");
        }

        // Create test result entry
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("Package", packageName);
        resultMap.put("Method", result.getMethod().getMethodName());
        resultMap.put("Status", status);
        resultMap.put("Remark", remark);

        // Add the result to the shared list
        synchronized (testCasesResultMap) {
            testCasesResultMap.add(resultMap);
        }
        LoggerUtil.info("Test Result Added: " + resultMap);
    }
}