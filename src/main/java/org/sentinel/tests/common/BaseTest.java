package org.sentinel.tests.common;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.sentinel.tests.enums.BrowserType;
import org.sentinel.tests.enums.OSType;
import org.sentinel.tests.reportUtils.PDFReport;
import org.sentinel.tests.testng.TestNGParamStore;
import org.sentinel.tests.ui.common.WebDriverManager;
import org.sentinel.tests.ui.common.pageObject.LoginPage;
import org.sentinel.tests.utils.ExcelUtil;
import org.sentinel.tests.utils.HandleFile;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Swapnil Damate
 */
public class BaseTest {
    protected WebDriver driver;
    protected LogAssert logAssert = new LogAssert();
    protected String osType;
    protected String browserType;
    protected String appURL;

    protected List<Map<String, String>> testCasesResultMap = new ArrayList<>();

    protected LoginPage loginPage;

    @BeforeSuite(alwaysRun = true)
    public void setupResultData() {
        Path allure_report = Paths.get("allure-report");
        HandleFile.deleteDir(allure_report);
        Path allure_results = Paths.get("reports/allure-results");
        HandleFile.deleteDir(allure_results);
        Path excel_report = Paths.get("reports/excel-report");
        HandleFile.deleteDir(excel_report);
        Path pdf_report = Paths.get("reports/pdf-report");
        HandleFile.deleteDir(pdf_report);
        ExcelUtil.createExcelFile();
    }

    /*
     * This is used for open the browser.
     */
    @Step
    @BeforeClass
    public void openBrowser(ITestContext context) {
        TestNGParamStore.loadParameters(context);
        osType = OSType.getOSType(TestNGParamStore.getParameter("operatingsystem"));
        browserType = BrowserType.getBrowserType(TestNGParamStore.getParameter("browser"));
        appURL = TestNGParamStore.getParameter("appURL");
        driver = WebDriverManager.getDriver(osType, browserType);
        context.setAttribute("driver", driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    /**
     * In this method we create all object of page factory.
     */
    @BeforeMethod(alwaysRun = true)
    public void initPageFactory() {
        loginPage = new LoginPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

    }

    /**
     * This is used for close the browser.
     */
    @AfterClass
    public void closeBrowser(ITestContext context) {

    }

    @AfterSuite(alwaysRun = true)
    public void pushResultData() {
//        driver.close();
//        WebDriverManager.quitDriver();

        PDFReport.generatePDF();
    }
}