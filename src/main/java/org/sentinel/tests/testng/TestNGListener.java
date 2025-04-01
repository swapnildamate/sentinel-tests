package org.sentinel.tests.testng;

import org.sentinel.tests.utils.ExcelUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNGListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        addTestResult(result, "Pass", "Test executed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
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
            ExcelUtil.addTestCases(testCasesResultMap);
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