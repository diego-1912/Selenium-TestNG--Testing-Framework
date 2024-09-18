package my.project.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.BaseClass;
import pages.LoginPage;

@Listeners(utils.TestListener.class)
public class LoginTest extends BaseClass {

    // Page object for LoginPage and logger instance
    private LoginPage loginPage;
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);
    private SoftAssert softAssert;

    /**
     * Initializes the test setup before each test method.
     * Creates a new instance of LoginPage and initializes SoftAssert.
     */
    @BeforeMethod
    public void setUp() {
        this.loginPage = new LoginPage(driver); // Initialize LoginPage object
        this.softAssert = new SoftAssert(); // Initialize SoftAssert for flexible assertions
        logger.info("LoginPage created for new test.");
    }

    /**
     * Test to verify the login functionality with an invalid username.
     */
    @Test
    public void loginWithInvalidUsername() {
        logger.info("Starting loginWithInvalidUsername test.");
        loginPage.enterUsername("invalid_user"); // Enter an invalid username
        loginPage.enterPassword("secret_sauce"); // Enter a valid password
        loginPage.clickLogin(); // Attempt to login
        String actualErrorMessage = loginPage.invalidGetUserNameAndPasswordErrorMessage(); // Capture the error message
        logger.info("Actual error message: {}", actualErrorMessage);
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        // Assert that the actual error message matches the expected message
        softAssert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message mismatch for invalid username.");
        softAssert.assertAll();
    }

    /**
     * Test to verify the login functionality with an incorrect password.
     */
    @Test
    public void loginWithIncorrectPassword() {
        logger.info("Starting loginWithIncorrectPassword test.");
        loginPage.enterUsername("standard_user"); // Enter a valid username
        loginPage.enterPassword("incorrect_password"); // Enter an incorrect password
        loginPage.clickLogin(); // Attempt to login
        String actualErrorMessage = loginPage.invalidGetUserNameAndPasswordErrorMessage(); // Capture the error message
        logger.info("Actual error message: {}", actualErrorMessage);
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        // Assert that the actual error message matches the expected message
        softAssert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message mismatch for incorrect password.");
        softAssert.assertAll();
    }

    /**
     * Test to verify the login functionality with an empty password.
     */
    @Test
    public void loginWithEmptyPassword() {
        logger.info("Starting loginWithEmptyPassword test.");
        loginPage.enterUsername("standard_user"); // Enter a valid username
        loginPage.enterPassword(""); // Enter an empty password
        loginPage.clickLogin(); // Attempt to login
        String actualErrorMessage = loginPage.emptyGetPasswordErrorMessage(); // Capture the error message
        logger.info("Actual error message: {}", actualErrorMessage);
        String expectedErrorMessage = "Epic sadface: Password is required";
        // Assert that the actual error message matches the expected message
        softAssert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message mismatch for empty password.");
        softAssert.assertAll();
    }

    /**
     * Test to verify the login functionality with an empty username.
     */
    @Test
    public void loginWithEmptyUsername() {
        logger.info("Starting loginWithEmptyUsername test.");
        loginPage.enterUsername(""); // Enter an empty username
        loginPage.enterPassword("secret_sauce"); // Enter a valid password
        loginPage.clickLogin(); // Attempt to login
        String actualErrorMessage = loginPage.emptyGetUserNameErrorMessage(); // Capture the error message
        logger.info("Actual error message: {}", actualErrorMessage);
        String expectedErrorMessage = "Epic sadface: Username is required";
        // Assert that the actual error message matches the expected message
        softAssert.assertEquals(actualErrorMessage, expectedErrorMessage, "Error message mismatch for empty username.");
        softAssert.assertAll();
    }

    /**
     * Test to verify the login functionality with valid credentials.
     */
    @Test
    public void validUserLogin() {
        logger.info("Starting the validUserLogin test.");
        loginPage.enterUsername("standard_user"); // Enter a valid username
        loginPage.enterPassword("secret_sauce"); // Enter a valid password
        loginPage.clickLogin(); // Attempt to login
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        String actualUrl = getCurrentUrl(); // Get the current URL after login
        logger.info("Current URL after login: {}", actualUrl);
        // Assert that the actual URL matches the expected URL
        softAssert.assertEquals(actualUrl, expectedUrl, "User was not redirected to the inventory page after valid login.");
        softAssert.assertAll();
    }
}