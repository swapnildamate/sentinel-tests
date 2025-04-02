package org.sentinel.tests.utils.testng;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.utils.log.Logger;
import org.sentinel.tests.utils.insights.TakeScreenshot;
import org.sentinel.tests.utils.ExcelUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.*;

public class ITestListeners implements ITestListener {
    private static WebDriver driver;
    protected List<Map<String, String>> testCasesResultMap = new ArrayList<>();

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        Logger.info("********** Test Execution Started.....**********");
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
            TakeScreenshot.captureScreenshot(driver);
            Logger.info("Screenshot not captured.");// Capture screenshot
        } else {
            Logger.warning("Driver is null. Screenshot not captured.");
        }
        addTestResult(result, "Fail", result.getThrowable() != null ? result.getThrowable().getMessage() : "No error message");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        addTestResult(result, "Skip", "Test was skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        Logger.info("********** Test Execution Completed.....**********");
        testCasesResultMap = (List<Map<String, String>>) context.getAttribute("testCasesResultMap");
        Logger.warning(testCasesResultMap.toString());
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
            Logger.info("Package Name: " + packageName);
        } else {
            Logger.info("No package found. Class might be in the default package.");
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
        Logger.info("Test Result Added: " + resultMap);
    }

}