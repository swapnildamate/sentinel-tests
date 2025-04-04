package org.sentinel.tests.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.sentinel.tests.utils.log.LoggerUtil;

public class MyAccountPage {
    private WebDriver driver;

    @FindBy(xpath = "//a[@href='/account']")
    protected WebElement accountBtn;

    @FindBy(xpath = "//h1[contains(text(),'My Account')]")
    protected WebElement pageHeader;

    @FindBy(xpath = "//button[contains(text(),'Log out')]")
    protected WebElement logoutBtn;

    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public boolean isDisplayedAccoutBtn() {
        LoggerUtil.info("Looking account button.");
        return accountBtn.isDisplayed();
    }

    /**
     * Clicks on the account button.
     * This method logs the action and performs a click operation on the account button element.
     */
    public void clickOnAccount() {
        LoggerUtil.info("Clicking on account button.");
        accountBtn.click();
    }

    public String getPageHeader() {
        LoggerUtil.info("Getting account page header.");
        return pageHeader.getText().trim();
    }

    public boolean isDisplayedLogoutBtn() {
        LoggerUtil.info("Looking for Logout button.");
        return logoutBtn.isDisplayed();
    }

    public void clickOnLogoutBtn() {
        LoggerUtil.info("Clicking on login button.");
        logoutBtn.click();
    }

}
