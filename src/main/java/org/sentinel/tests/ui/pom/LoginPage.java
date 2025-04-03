package org.sentinel.tests.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.sentinel.tests.utils.log.LoggerUtil;

/**
 * This class represents the Login Page of the application and provides methods
 * to interact with its elements such as account button, email input, password input,
 * and login button.
 *
 * <p>It uses the Page Object Model (POM) design pattern and Selenium's PageFactory
 * for initializing web elements.</p>
 *
 * <p>Annotations:</p>
 * <ul>
 *   <li>@FindBy: Used to locate web elements on the page using XPath.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #isDisplayedAccoutButton()}: Checks if the account button is displayed.</li>
 *   <li>{@link #clickOnAccount()}: Clicks on the account button.</li>
 *   <li>{@link #getPageHeader()}: Retrieves the header text of the login page.</li>
 *   <li>{@link #isDisplayedEmailField()}: Checks if the email input field is displayed.</li>
 *   <li>{@link #enterEmail(String)}: Clears and enters the provided email into the email input field.</li>
 *   <li>{@link #isPasswordField()}: Checks if the password input field is displayed.</li>
 *   <li>{@link #enterPassword(String)}: Clears and enters the provided password into the password input field.</li>
 *   <li>{@link #isDisplayedLoginButton()}: Checks if the login button is displayed.</li>
 *   <li>{@link #clickOnLoginButton()}: Clicks on the login button.</li>
 * </ul>
 *
 * <p>Constructor:</p>
 * <ul>
 *   <li>{@link #LoginPage(WebDriver)}: Initializes the web elements using the provided WebDriver instance.</li>
 * </ul>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>LoggerUtil: Used for logging information during method execution.</li>
 *   <li>WebDriver: Selenium WebDriver instance for interacting with the browser.</li>
 * </ul>
 *
 * @author Swapnil Damate
 */
public class LoginPage {
    private WebDriver driver;

    @FindBy(xpath = "//button[contains(@data-action,'slideover-account')]")
    protected WebElement accountBtn;

    @FindBy(xpath = "//div[@id='slideover-account']//h2[contains(text(),'Login')]")
    protected WebElement pageHeader;

    @FindBy(xpath = "//div[@id='slideover-account']//input[contains(@type,'email')]")
    protected WebElement emailInput;

    @FindBy(xpath = "//div[@id='slideover-account']//input[contains(@type,'password')]")
    protected WebElement passwordInput;

    @FindBy(xpath = "//div[@id='slideover-account']//input[@class='input-checkbox']")
    protected WebElement rememberCheckbox;

    @FindBy(xpath = "//div[@id='slideover-account']//input[contains(@value,'Login')]")
    protected WebElement loginBtn;

    @FindBy(xpath = "//div[@id='slideover-account']//a[contains(text(),'Sign Up')]")
    protected WebElement signUplnk;

    @FindBy(xpath = "//div[@id='slideover-account']//a[contains(text(),'Forgot password')]")
    protected WebElement forgotPasswordlnk;

    @FindBy(xpath = "//div[@data-controller='alert']//p")
    protected WebElement loginLogotTxt;

    /**
     * Constructor for the LoginPage class.
     * Initializes the web elements on the page using the provided WebDriver instance.
     *
     * @param driver the WebDriver instance used to interact with the web page
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Checks if the account button is displayed on the page.
     *
     * @return {@code true} if the account button is displayed, {@code false} otherwise.
     */
    public boolean isDisplayedAccoutButton() {
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

    /**
     * Retrieves the text of the page header element.
     *
     * @return A trimmed string representing the text of the page header.
     */
    public String getPageHeader() {
        LoggerUtil.info("Getting login page header.");
        return pageHeader.getText().trim();
    }


    /**
     * Checks if the email input field is displayed on the page.
     *
     * @return {@code true} if the email input field is visible, {@code false} otherwise.
     */
    public boolean isDisplayedEmailField() {
        LoggerUtil.info("Looking email field.");
        return emailInput.isDisplayed();
    }

    /**
     * Clears the email input field and enters the provided email address.
     *
     * @param email The email address to be entered into the email input field.
     */
    public void enterEmail(String email) {
        LoggerUtil.info("Entering email.....");
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    /**
     * Checks if the password input field is displayed on the page.
     *
     * @return {@code true} if the password input field is visible, {@code false} otherwise.
     */
    public boolean isPasswordField() {
        LoggerUtil.info("Looking for password field.");
        return passwordInput.isDisplayed();
    }

    /**
     * Clears the password input field and enters the provided password.
     *
     * @param password The password to be entered into the password input field.
     */
    public void enterPassword(String password) {
        LoggerUtil.info("Entering password.....");
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    /**
     * Checks if the login button is displayed on the page.
     *
     * @return {@code true} if the login button is visible, {@code false} otherwise.
     */
    public boolean isDisplayedLoginButton() {
        LoggerUtil.info("Looking for login button.");
        return loginBtn.isDisplayed();
    }

    /**
     * Clicks on the login button.
     * This method logs the action and performs a click operation on the login button element.
     */
    public void clickOnLoginButton() {
        LoggerUtil.info("Clicking on login button.");
        loginBtn.click();
    }

    public boolean isDisplayedRememberMeCheckbox() {
        LoggerUtil.info("Looking for Remember Me Checkbox.");
        return rememberCheckbox.isDisplayed();
    }

    public boolean isDisplayedSignUpLnk() {
        LoggerUtil.info("Looking for Sign Up.");
        return signUplnk.isDisplayed();
    }

    public boolean isDisplayedForgotPasswordLnk() {
        LoggerUtil.info("Looking for Forgot password.");
        return forgotPasswordlnk.isDisplayed();
    }

    public String getLoginLogoutSuccessMsg() {
        return loginLogotTxt.getText().trim();
    }
}