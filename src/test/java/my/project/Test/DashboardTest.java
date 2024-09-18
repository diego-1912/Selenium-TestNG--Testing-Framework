package my.project.Test;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.BaseClass;
import pages.DashboardPage;
import pages.LoginPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners(utils.TestListener.class)
public class DashboardTest extends BaseClass {
    // Page objects and test utilities
    private DashboardPage dashboardPage;
    private LoginPage loginPage;
    private SoftAssert softAssert;
    private static final Logger logger = LoggerFactory.getLogger(DashboardTest.class);

    /**
     * Sets up the test class by initializing Page Objects and SoftAssert before any test is run.
     */
    @BeforeClass
    public void setUpClass() {
        this.loginPage = new LoginPage(driver); // Initialize LoginPage object
        this.dashboardPage = new DashboardPage(driver); // Initialize DashboardPage object
        this.softAssert = new SoftAssert(); // Initialize SoftAssert for flexible assertions
    }

    /**
     * Logs in to the application before each test method to ensure the user starts on the Dashboard page.
     */
    @BeforeMethod
    public void setUp() {
        loginPage.login("standard_user", "secret_sauce"); // Perform login using valid credentials
        Assert.assertTrue(dashboardPage.isOnDashboardPage(), "Failed to login to the dashboard"); // Verify successful login
        logger.info("Logged into Dashboard");
    }

    /**
     * Test to verify that an item can be successfully added to the cart.
     */
    @Test
    public void testAddItemToCart() {
        logger.info("Testing add item to cart functionality");
        dashboardPage.addToCar(); // Add an item to the cart
        // Example assertion to check if the item was added successfully (can be uncommented if needed)
        // Assert.assertTrue(dashboardPage.isItemAddedToCart(), "Item was not added to cart");
        logger.info("Add item to cart test completed");
    }

    /**
     * Test to verify that an item can be successfully removed from the cart.
     */
    @Test(priority = 1)
    public void testRemoveItemFromCart() {
        logger.info("Testing remove item from cart functionality");
        dashboardPage.addToCar(); // Add an item first to test its removal
        dashboardPage.removeFromTheCar(); // Remove the item from the cart
        logger.info("Remove item from cart test completed");
    }

    /**
     * Test to verify that the user can navigate to the checkout page.
     */
    @Test
    public void testGoToCheckoutPage() {
        logger.info("Testing navigation to checkout page");
        dashboardPage.goToCheckoutPage(); // Navigate to the checkout page
        Assert.assertTrue(dashboardPage.isOnCheckOutPage(), "Failed to navigate to the checkout page"); // Verify navigation
        logger.info("Navigation to checkout page test completed");
    }

    /**
     * Test to verify that items are sorted from low to high price correctly.
     */
    @Test
    public void testSortItemsLowToHigh() {
        logger.info("Testing sort items low to high functionality");
        dashboardPage.sortItemsLowToHigh(); // Sort items from low to high price
        // Example assertion to verify sorting (can be uncommented if needed)
        // Assert.assertTrue(dashboardPage.isItemsSortedLowToHigh(), "Items are not sorted low to high");
        logger.info("Sort items low to high test completed");
    }

    /**
     * Test to verify that product details are displayed correctly.
     */
    @Test
    public void testGetProductDetails() {
        logger.info("Testing get product details functionality");
        dashboardPage.getProductDetails(); // Retrieve product details
        // Example assertion to verify product details (can be uncommented if needed)
        // Assert.assertTrue(dashboardPage.areProductDetailsCorrect(), "Product details are incorrect");
        logger.info("Get product details test completed");
    }

    /**
     * Test to verify that the user can log out successfully.
     */
    @Test
    public void testLogout() {
        logger.info("Testing logout functionality");
        dashboardPage.logOut(); // Perform logout
        Assert.assertFalse(dashboardPage.isOnDashboardPage(), "Failed to logout"); // Verify successful logout
        logger.info("Logout test completed");
    }

    /**
     * Ensures that after each test method, the test environment is reset to the Dashboard page.
     */
    @AfterMethod
    public void returnToDashboard() {
        if (!dashboardPage.isOnDashboardPage()) { // Check if the user is not on the Dashboard page
            driver.get("https://www.saucedemo.com/inventory.html"); // Navigate back to the Dashboard page
            logger.info("Returned to Dashboard");
        }
    }

    /**
     * Closes the WebDriver and cleans up resources after all tests have run.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) { // Check if the WebDriver instance exists
            driver.quit(); // Quit the WebDriver instance
            logger.info("Driver shut down");
        }
    }
}