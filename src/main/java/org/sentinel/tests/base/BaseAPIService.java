package org.sentinel.tests.base;

import org.sentinel.tests.api.collection.restfulbooker.BookingService;
import org.sentinel.tests.utils.ExcelUtil;
import org.sentinel.tests.utils.FileUtil;
import org.sentinel.tests.utils.insights.PDFReport;
import org.sentinel.tests.utils.testng.AssertLog;
import org.sentinel.tests.utils.testng.ReadTestNG;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseAPIService {
    protected AssertLog assertLog = new AssertLog();
    protected String baseURI;
    protected BookingService bookingService;

    @BeforeSuite(alwaysRun = true)
    public void cleanUpPreviousData() {
        Path allure_report = Paths.get("allure-report");
        FileUtil.deleteDir(allure_report);
        Path allure_results = Paths.get("reports/allure-results");
        FileUtil.deleteDir(allure_results);
        Path excel_report = Paths.get("reports/excel-report");
        FileUtil.deleteDir(excel_report);
        Path pdf_report = Paths.get("reports/pdf-report");
        FileUtil.deleteDir(pdf_report);
        ExcelUtil.createExcelFile();
    }

    /*
     * This is used for open the browser.
     */
    @BeforeClass
    public void setUpEnv(ITestContext context) {
        ReadTestNG.loadParameters(context);
        baseURI = ReadTestNG.getParameter("baseURI");
    }

    @BeforeMethod
    public void setUpService() {
        bookingService = new BookingService(baseURI);
    }

    @AfterSuite(alwaysRun = true)
    public void generateReport() {
        PDFReport.generatePDF();
    }
}
