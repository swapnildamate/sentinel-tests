package org.sentinel.tests.common;

import org.openqa.selenium.WebDriver;
import org.sentinel.tests.enums.BrowserType;
import org.sentinel.tests.enums.OSType;
import org.sentinel.tests.testng.TestNGParamStore;
import org.sentinel.tests.ui.common.WebDriverManager;
import org.sentinel.tests.ui.common.pageObject.LoginPage;
import org.sentinel.tests.utils.ExcelUtil;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Swapnil Damate
 */
public class BaseTest {
    protected WebDriver driver;
    protected LogAssert logAssert=new LogAssert();
    protected String osType;
    protected String browserType;
    protected String appURL;

    protected List<Map<String, String>> testCasesResultMap = new ArrayList<>();

    protected LoginPage loginPage;

    @BeforeSuite
    public void setupResultData() {
        ExcelUtil.createExcelFile();
    }

    /*
     * This is used for open the browser.
     */
    @BeforeClass(alwaysRun = true)
    public void openBrowser(ITestContext context) {
        TestNGParamStore.loadParameters(context);
        osType = OSType.getOSType(TestNGParamStore.getParameter("operatingsystem"));
        browserType = BrowserType.getBrowserType(TestNGParamStore.getParameter("browser"));
        appURL = TestNGParamStore.getParameter("appURL");
        driver= WebDriverManager.getDriver(osType,browserType);
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

    /**
     * This is used for close the browser.
     */
    @AfterClass(alwaysRun = true)
    public void closeBrowser() {
        driver.close();
        WebDriverManager.quitDriver();
    }

    @AfterSuite(enabled = true)
    public void pushResultData() {
        if (!testCasesResultMap.isEmpty()) {
            ExcelUtil.addTestCases(testCasesResultMap);
        }
    }
}
