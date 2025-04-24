package org.sentinel.tests.ui.spreecom;

import io.qameta.allure.Description;
import org.sentinel.tests.base.BaseUIService;
import org.sentinel.tests.utils.ExcelUtil;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;

public class LogoutTest extends BaseUIService {
    private boolean result;
    private String dataFile = "Users.xlsx";

    @Test
    @Description("Verify user able to logout.")
    public void T0201(Method method) throws Exception {

        String methodName =method.getName();
        Description description = method.getAnnotation(Description.class);
        LoggerUtil.info(String.format("Test Case : %s Description :%s", methodName, description.value()));

        Map<String, String> testData = ExcelUtil.readKeyValuePairs(dataFile, "user1");

        driver.get(appURL);

        //Step 1
        LoggerUtil.info("Precondition Started.....");
        loginPage.clickOnAccount();
        loginPage.enterEmail(testData.get("name"));
        loginPage.enterPassword(testData.get("password"));
        loginPage.clickOnLoginButton();
        String actualSignMsg = loginPage.getLoginLogoutSuccessMsg();
        LoggerUtil.info(String.format("Actual Msg : %s", actualSignMsg));
        String expectedSignMsg = "SIGNED IN SUCCESSFULLY.";
        assertLog.assertEquals(actualSignMsg, expectedSignMsg, "User signed is successfully.", "User un-authorized.");
        LoggerUtil.info("Precondition Completed.....");

        LoggerUtil.info("Step 1: Started.....");
        result = myAccountPage.isDisplayedAccoutBtn();
        assertLog.assertTrue(result, "Step 1: My Account button is displayed.", "Step 1: My Account button is not displayed.");
        myAccountPage.clickOnAccount();
        LoggerUtil.info("Step 2: Started.....");
        String actualPageHeader = myAccountPage.getPageHeader();
        String expectedPageHader = "MY ACCOUNT";
        assertLog.assertString(actualPageHeader, expectedPageHader, "Step 2: My Account page header matched.", "Step 2: My Account page header mis-matched.");

        LoggerUtil.info("Step 3: Started.....");
        result = myAccountPage.isDisplayedLogoutBtn();
        assertLog.assertTrue(result, "Step 3: Logout button is displayed.", "Step 3: Logout button is not displayed.");

        LoggerUtil.info("Step 4: Started.....");
        myAccountPage.clickOnLogoutBtn();
        String actualLogoutMsg = loginPage.getLoginLogoutSuccessMsg();
        LoggerUtil.info(String.format("Actual Msg : %s", actualLogoutMsg));
        String expectedLogoutMsg = "SIGNED OUT SUCCESSFULLY.";
        assertLog.assertEquals(actualLogoutMsg, expectedLogoutMsg, "Step 4: User Logged successfully.", "Step 4: Unable to logout.");

        assertLog.assertAllWithLog();
    }

}
