package org.sentinel.tests.testng;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.common.LoggerUtil;
import org.sentinel.tests.reportUtils.ScreenshotUtil;
import org.sentinel.tests.utils.ExcelUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.*;

public class TestNGListener implements ITestListener {
    private static WebDriver driver;
    protected List<Map<String, String>> testCasesResultMap = new ArrayList<>();

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        LoggerUtil.info("********** Test Execution Started.....**********");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        addTestResult(result, "Pass", "Test executed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestContext context = result.getTestContext();
        driver = (WebDriver) context.getAttribute("driver");
        if (driver != null) {
            ScreenshotUtil.captureScreenshot(driver);
            LoggerUtil.info("Screenshot not captured.");// Capture screenshot
        } else {
            LoggerUtil.warning("Driver is null. Screenshot not captured.");
        }
        addTestResult(result, "Fail", result.getThrowable() != null ? result.getThrowable().getMessage() : "No error message");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        addTestResult(result, "Skip", "Test was skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info("********** Test Execution Completed.....**********");
        testCasesResultMap = (List<Map<String, String>>) context.getAttribute("testCasesResultMap");
        LoggerUtil.warning(testCasesResultMap.toString());
        if (!testCasesResultMap.isEmpty()) {
            ExcelUtil.addTestCases(testCasesResultMap);
        }
    }

    private void addTestResult(ITestResult result, String status, String remark) {
        ITestContext context = result.getTestContext();

        // Retrieve or initialize testCasesResultMap
        List<Map<String, String>> testCasesResultMap = (List<Map<String, String>>) context.getAttribute("testCasesResultMap");

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
        LoggerUtil.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+testCasesResultMap);
    }

}