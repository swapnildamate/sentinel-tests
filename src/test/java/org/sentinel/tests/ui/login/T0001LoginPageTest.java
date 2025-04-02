package org.sentinel.tests.ui.login;

import io.qameta.allure.Description;
import org.sentinel.tests.common.BaseTest;
import org.sentinel.tests.common.LoggerUtil;
import org.testng.annotations.Test;


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
 *   <li>Relies on the logAssert utility for assertion logging.</li>
 *   <li>Utilizes LoggerUtil for logging information.</li>
 * </ul>
 * 
 * @author Swapnil Damate
 * 
 */
public class T0001LoginPageTest extends BaseTest {
    private boolean result;

    @Test
    @Description("Verify login page.")
    public void T0001() {
        driver.get(appURL);

        //Step 1
        LoggerUtil.info("Step 1: Started.....");
        result=loginPage.isDisplayedAccoutButton();
        logAssert.assertTrue(result,"Step 1: Account button is displayed.","Step 1: Account button is not displayed.");
        loginPage.clickOnAccount();

        //Step 2
        LoggerUtil.info("Step 2: Started.....");
        String pageHeader=loginPage.getPageHeader();
        LoggerUtil.info(String.format("Actual page header is: %s",pageHeader));
        String expectedPageHeader="Account";
        logAssert.assertEquals(pageHeader,expectedPageHeader,"Step 2: Account page header matched.","Step 2: Account page header mismatched.");

        //Step 3
        LoggerUtil.info("Step 3: Started.....");
        result = loginPage.isDisplayedEmailField();
        logAssert.assertTrue(result,"Step 3: Email field is displayed.","Step 3: Email field is not displayed.");

        //Step 4
        LoggerUtil.info("Step 4: Started.....");
        result = loginPage.isPasswordField();
        logAssert.assertTrue(result,"Step 4: Password field is displayed.","Step 4: Password field is not displayed.");

        logAssert.assertAllWithLog();
    }

    @Test
    @Description("Verify user able to login into system.")
    public void T0002() {

        driver.get(appURL);

        //Step 1
        LoggerUtil.info("Step 1: Started.....");
        result=loginPage.isDisplayedAccoutButton();
        logAssert.assertTrue(result,"Step 1: Account button is displayed.","Step 1: Account button is not displayed.");
        loginPage.clickOnAccount();

        //Step 2
        LoggerUtil.info("Step 2: Started.....");
        loginPage.enterEmail("abc@gmail.com");

        //Step 3
        LoggerUtil.info("Step 3: Started.....");
        loginPage.enterPassword("Password@123");

        //Step 4
        loginPage.clickOnLoginButton();

        //Step 5
        String actualMsg=loginPage.getSignedSuccessMsg();
        LoggerUtil.info(String.format("Actual Msg : %s",actualMsg));
        String expectedMsg="SIGNED IN SUCCESSFULLY.";
        logAssert.assertEquals(actualMsg,expectedMsg,"Step 5: User signed is successfully.","Step 5: User un-authorized.");

        logAssert.assertAllWithLog();
    }



}
