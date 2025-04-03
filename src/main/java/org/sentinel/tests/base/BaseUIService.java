package org.sentinel.tests.base;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.ui.pom.MyAccountPage;
import org.sentinel.tests.utils.testng.AssertLog;
import org.sentinel.tests.enums.BrowserType;
import org.sentinel.tests.enums.OSType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Swapnil Damate
 */
public class BaseUIService {
    protected WebDriver driver;
    protected AssertLog assertLog = new AssertLog();
    protected String osType;
    protected String browserType;
    protected String appURL;

    protected List<Map<String, String>> testCasesResultMap = new ArrayList<>();

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
        ExcelUtil.createExcelFile();
    }

    /*
     * This is used for open the browser.
     */
    @BeforeClass
    public void setUpEnv(ITestContext context) {
        ReadTestNG.loadParameters(context);
        osType = OSType.getOSType(ReadTestNG.getParameter("operatingsystem"));
        browserType = BrowserType.getBrowserType(ReadTestNG.getParameter("browser"));
        appURL = ReadTestNG.getParameter("appURL");
    }

    /**
     * In this method we create all object of page factory.
     */
    @BeforeMethod(alwaysRun = true)
    public void tearUp(ITestContext context) {
        driver = WebDriverManager.getDriver(osType, browserType);
        context.setAttribute("driver", driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();

        //init page object
        loginPage = new LoginPage(driver);
        myAccountPage=new MyAccountPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            WebDriverManager.quitDriver();
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
        PDFReport.generatePDF();
    }
}