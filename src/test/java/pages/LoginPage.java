package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BaseClass {

    // Logger for logging messages related to LoginPage actions
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Define locators for the web elements used in the LoginPage
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By emptyUserNameErrorMessage = By.xpath("//*[contains(text(), 'Epic sadface: Username is required')]");
    private final By emptyPasswordErrorMessage = By.xpath("//*[contains(text(), 'Epic sadface: Password is required')]");
    private final By invalidUserNameAndPasswordErrorMessage = By.xpath("//*[contains(text(),'Epic sadface: Username and password do not match any user in this service')]");

    /**
     * Constructor for the LoginPage class.
     * Initializes the WebDriver and the elements on the page.
     *
     * @param driver The WebDriver instance used to control the browser.
     */
    public LoginPage(WebDriver driver) {
        super(); // Call the constructor of the parent class (BaseClass)
        BaseClass.driver = driver; // Set the driver in the BaseClass
        PageFactory.initElements(driver, this); // Initialize web elements using PageFactory
        logger.info("LoginPage initialized");
    }

    /**
     * Enters the username in the username field.
     *
     * @param username The username to be entered.
     */
    public void enterUsername(String username) {
        sendKeys(usernameField, username); // Enter the username into the username field
    }

    /**
     * Enters the password in the password field.
     *
     * @param password The password to be entered.
     */
    public void enterPassword(String password) {
        sendKeys(passwordField, password); // Enter the password into the password field
    }

    /**
     * Clicks the login button.
     */
    public void clickLogin() {
        click(loginButton); // Click the login button to attempt login
    }

    /**
     * Retrieves the error message displayed for an empty username field.
     *
     * @return The error message text.
     */
    public String emptyGetUserNameErrorMessage() {
        return getText(emptyUserNameErrorMessage); // Get the text of the error message for an empty username
    }

    /**
     * Retrieves the error message displayed for an empty password field.
     *
     * @return The error message text.
     */
    public String emptyGetPasswordErrorMessage() {
        return getText(emptyPasswordErrorMessage); // Get the text of the error message for an empty password
    }

    /**
     * Retrieves the error message displayed for invalid username and password combination.
     *
     * @return The error message text.
     */
    public String invalidGetUserNameAndPasswordErrorMessage() {
        return getText(invalidUserNameAndPasswordErrorMessage); // Get the text of the error message for invalid credentials
    }

    /**
     * Performs the login action using the provided username and password.
     *
     * @param username The username to be entered.
     * @param password The password to be entered.
     */
    public void login(String username, String password) {
        enterUsername(username); // Enter the username
        enterPassword(password); // Enter the password
        clickLogin(); // Click the login button
        logger.info("Logged in with username: {}", username);
    }
}
