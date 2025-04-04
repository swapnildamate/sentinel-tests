package org.sentinel.tests.ui.spreecom;

import io.qameta.allure.Description;
import org.sentinel.tests.base.BaseUIService;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.ExcelUtil;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * The LoginTest class is a test class that extends the BaseTestUI class.
 * It contains test methods to validate the login functionality of the application.
 *
 * <p>This class includes the following functionality:</p>
 * <ul>
 *   <li>Verifies if the account button is displayed on the login page.</li>
 *   <li>Clicks on the account button and retrieves the page header.</li>
 *   <li>Checks if the email and password fields are displayed.</li>
 * </ul>
 *
 * <p>Test Method:</p>
 * <ul>
 *   <li><b>T0001:</b> Validates the visibility of UI elements and interactions on the login page.</li>
 * </ul>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>Requires the BaseTestUI class for test setup and teardown.</li>
 *   <li>Uses the loginPage object for interacting with the login page elements.</li>
 *   <li>Relies on the assertLog utility for assertion logging.</li>
 *   <li>Utilizes LoggerUtil for logging information.</li>
 * </ul>
 *
 * @author Swapnil Damate
 */
public class LoginTest extends BaseUIService {
    private boolean result;
    private String dataFile="Users.xlsx";

    @Test
    @Description("Verify login page.")
    public void T0101() throws Exception {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Method method = this.getClass().getMethod(methodName);
        Description description = method.getAnnotation(Description.class);
        LoggerUtil.info(String.format("Test Case : %s Description :%s", methodName, description.value().toString()));

        //Read Test Data
        Map<String, String> testData = ExcelUtil.readKeyValuePairs(dataFile, "user1");

        driver.get(appURL);

        //Step 1
        LoggerUtil.info("Step 1: Started.....");
        result = loginPage.isDisplayedAccoutButton();
        assertLog.assertTrue(result, "Step 1: Account button is displayed.", "Step 1: Account button is not displayed.");
        loginPage.clickOnAccount();

        //Step 2
        LoggerUtil.info("Step 2: Started.....");
        String pageHeader = loginPage.getPageHeader();
        LoggerUtil.info(String.format("Actual page header is: %s", pageHeader));
        String expectedPageHeader = "Login";
        assertLog.assertEquals(pageHeader, expectedPageHeader, "Step 2: Account page header matched.", "Step 2: Account page header mismatched.");

        //Step 3
        LoggerUtil.info("Step 3: Started.....");
        result = loginPage.isDisplayedEmailField();
        assertLog.assertTrue(!result, "Step 3: Email field is displayed.", "Step 3: Email field is not displayed.");

        //Step 4
        LoggerUtil.info("Step 4: Started.....");
        result = loginPage.isPasswordField();
        assertLog.assertTrue(result, "Step 4: Password field is displayed.", "Step 4: Password field is not displayed.");

        //Step 5
        LoggerUtil.info("Step 5: Started.....");
        result = loginPage.isDisplayedRememberMeCheckbox();
        assertLog.assertTrue(!result, "Step 5: Remember Me Checkbox field is displayed.", "Step 5: Remember Me Checkbox field is not displayed.");

        //Step 5
        LoggerUtil.info("Step 6: Started.....");
        result = loginPage.isDisplayedForgotPasswordLnk();
        assertLog.assertTrue(result, "Step 6: Forgot Password Link is displayed.", "Step 6: Forgot Password Link is not displayed.");

        //Step 5
        LoggerUtil.info("Step 7: Started.....");
        result = loginPage.isDisplayedSignUpLnk();
        assertLog.assertTrue(result, "Step 7: Sign Up Link is displayed.", "Step 7: Sign Up Link is not displayed.");

        assertLog.assertAllWithLog();
    }

    @Test
    @Description("Verify user able to login into system.")
    public void T0102() throws Exception {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Method method = this.getClass().getMethod(methodName);
        Description description = method.getAnnotation(Description.class);
        LoggerUtil.info(String.format("Test Case : %s Description :%s", methodName, description.value().toString()));

        Map<String, String> testData = ExcelUtil.readKeyValuePairs(dataFile, "user1");

        driver.get(appURL);

        //Step 1
        LoggerUtil.info("Step 1: Started.....");
        result = loginPage.isDisplayedAccoutButton();
        assertLog.assertTrue(result, "Step 1: Account button is displayed.", "Step 1: Account button is not displayed.");
        loginPage.clickOnAccount();

        //Step 2
        LoggerUtil.info("Step 2: Started.....");
        loginPage.enterEmail(testData.get("name"));

        //Step 3
        LoggerUtil.info("Step 3: Started.....");
        loginPage.enterPassword(testData.get("password"));

        //Step 4
        loginPage.clickOnLoginButton();

        //Step 5
        String actualMsg = loginPage.getLoginLogoutSuccessMsg();
        LoggerUtil.info(String.format("Actual Msg : %s", actualMsg));
        String expectedMsg = "SIGNED IN SUCCESSFULLY.";
        assertLog.assertEquals(actualMsg, expectedMsg, "Step 5: User signed is successfully.", "Step 5: User un-authorized.");

        assertLog.assertAllWithLog();
    }

}
