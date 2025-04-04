/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.base;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.ui.pom.MyAccountPage;
import org.sentinel.tests.utils.insights.AllureEnvironmentSetup;
import org.sentinel.tests.utils.insights.CaptureAttachment;
import org.sentinel.tests.utils.insights.GetCapturedSession;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.testng.AssertLog;
import org.sentinel.tests.utils.insights.PDFReport;
import org.sentinel.tests.utils.testng.ReadTestNG;
import org.sentinel.tests.config.ui.WebDriverManager;
import org.sentinel.tests.ui.pom.LoginPage;
import org.sentinel.tests.utils.ExcelUtil;
import org.sentinel.tests.utils.FileUtil;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Base class for UI testing services providing common setup and tear down operations.
 * This class handles WebDriver initialization, test reporting setup, and page object management.
 * Key features:
 * - WebDriver setup and management
 * - Test reporting (Allure, Excel, PDF)
 * - Page object initialization
 * - Test environment cleanup
 * - Video capture for test sessions
 * The class uses TestNG annotations to manage the test lifecycle:
 * - @BeforeSuite: Cleans up previous test reports
 * - @BeforeClass: Sets up test environment and parameters
 * - @BeforeMethod: Initializes WebDriver and page objects
 * - @AfterMethod: Closes WebDriver and captures test video
 * - @AfterClass: Cleanup operations
 * - @AfterSuite: Generates test reports
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see WebDriverManager
 * @see LoginPage
 * @see MyAccountPage
 * @see AllureEnvironmentSetup
 * @see PDFReport
 */
public class BaseUIService {
    protected WebDriver driver;
    protected AssertLog assertLog = new AssertLog();
    protected String appURL;

    protected LoginPage loginPage;
    protected MyAccountPage myAccountPage;

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
        Path video = Paths.get("reports/video");
        FileUtil.deleteDir(video);
        ExcelUtil.createExcelFile();
    }

    /*
     * This is used for open the browser.
     */
    @BeforeClass
    public void setUpEnv(ITestContext context) {
        ReadTestNG.loadParameters(context);
        appURL = ReadTestNG.getParameter("appURL");
    }

    /**
     * In this method we create all object of page factory.
     */
    @BeforeMethod(alwaysRun = true)
    public void tearUp(ITestContext context) {
        driver = WebDriverManager.getDriverInstance();
        context.setAttribute("driver", driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
        //init page object
        loginPage = new LoginPage(driver);
        myAccountPage = new MyAccountPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            WebDriverManager.quitDriver();
        }

        LoggerUtil.warning(String.format("Current Session Id is: %s", WebDriverManager.sessionId));
        if (WebDriverManager.sessionId != null) {
            CaptureAttachment.captureVideo(GetCapturedSession.getVideoPath(WebDriverManager.sessionId));
        }
    }

    /**
     * This is used for close the browser.
     */
    @AfterClass
    public void closeBrowser(ITestContext context) {

    }

    @AfterSuite(alwaysRun = true)
    public void generateReport() {
        AllureEnvironmentSetup.createEnvironmentFile();
        PDFReport.generatePDF();
    }
}