package org.sentinel.tests.testng;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.common.LoggerUtil;
import org.sentinel.tests.reportUtils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNGListener implements ITestListener {
    private static WebDriver driver;

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

        // Retrieve and push results to Excel
        List<Map<String, String>> testCasesResultMap = (List<Map<String, String>>) context.getAttribute("testCasesResultMap");
        if (testCasesResultMap != null) {
            String resultExcelPath = (String) context.getAttribute("resultExcelPath");
            //ExcelUtil.addTestCases(testCasesResultMap);
        }
    }

    private void addTestResult(ITestResult result, String status, String remark) {
        ITestContext context = result.getTestContext();
        List<Map<String, String>> testCasesResultMap = (List<Map<String, String>>) context.getAttribute("testCasesResultMap");

        if (testCasesResultMap == null) {
            testCasesResultMap = new ArrayList<>();
            context.setAttribute("testCasesResultMap", testCasesResultMap);
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("Package", result.getTestClass().getName());
        resultMap.put("Method", result.getMethod().getMethodName());
        resultMap.put("Status", status);
        resultMap.put("Remark", remark);

        testCasesResultMap.add(resultMap);
    }
}