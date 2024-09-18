package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPage extends BaseClass {

    // Logger for logging messages related to DashboardPage actions
    private static final Logger logger = LoggerFactory.getLogger(DashboardPage.class);

    // Define locators for the web elements used in the DashboardPage
    private final By addToCartButton = By.xpath("//button[@id='add-to-cart-sauce-labs-bike-light']");
    private final By removeFromCartButton = By.xpath("//button[@id='remove-sauce-labs-bike-light']");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By checkoutButton = By.id("checkout");
    private final By sortDropdown = By.className("product_sort_container");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By lowToHighDropdownOption = By.xpath("//option[text()='Price (low to high)']");
    private final By lowPriceItem = By.xpath("//div[normalize-space()='Sauce Labs Fleece Jacket']");
    private final By productDetails = By.xpath("//div[@class='inventory_details_desc large_size']");

    // Constructor for the DashboardPage class
    public DashboardPage(WebDriver driver) {
        super(); // Call the constructor of the parent class (BaseClass)
    }

    /**
     * Adds an item to the cart.
     */
    public void addToCar() {
        logger.info("Adding item to cart");
        click(addToCartButton); // Click the 'Add to Cart' button
        waitForElementVisibility(removeFromCartButton); // Wait until the 'Remove from Cart' button is visible
        logger.info("Item added to cart");
    }

    /**
     * Logs the user out of the application.
     */
    public void logOut() {
        logger.info("Logging out");
        click(menuButton); // Click the menu button to open the sidebar
        click(logoutLink); // Click the logout link to log out
        logger.info("Logged out successfully");
    }

    /**
     * Removes an item from the cart.
     */
    public void removeFromTheCar() {
        logger.info("Removing item from cart");
        click(addToCartButton); // Click the 'Add to Cart' button (if not already clicked)
        click(removeFromCartButton); // Click the 'Remove from Cart' button to remove the item
        waitForElementVisibility(addToCartButton); // Wait until the 'Add to Cart' button is visible again
        logger.info("Item removed from cart");
    }

    /**
     * Navigates to the checkout page.
     */
    public void goToCheckoutPage() {
        logger.info("Navigating to checkout page");
        click(cartIcon); // Click the cart icon to view the cart
        click(checkoutButton); // Click the checkout button to proceed to the checkout page
        logger.info("Navigated to checkout page");
    }

    /**
     * Sorts the items by price from low to high.
     */
    public void sortItemsLowToHigh() {
        logger.info("Sorting items by price: low to high");
        click(sortDropdown); // Click the sort dropdown to display options
        click(lowToHighDropdownOption); // Select the 'Price (low to high)' option
        waitForElementVisibility(lowToHighDropdownOption); // Wait until the option is visible
        logger.info("Items sorted by price: low to high");
    }

    /**
     * Retrieves and logs the details of a product.
     */
    public void getProductDetails() {
        logger.info("Getting product details");
        click(sortDropdown); // Click the sort dropdown
        click(lowToHighDropdownOption); // Select 'Price (low to high)'
        click(lowPriceItem); // Click on the lowest-priced item
        waitForElementVisibility(productDetails); // Wait until the product details are visible
        String details = getElementText(productDetails); // Get the text of the product details
        logger.info("Product details: {}", details);
    }

    /**
     * Checks if the user is on the Dashboard page.
     *
     * @return True if on the Dashboard page, otherwise false.
     */
    public boolean isOnDashboardPage() {
        String currentUrl = driver.getCurrentUrl();
        boolean isOnPage = currentUrl.contains("https://www.saucedemo.com/inventory.html");
        logger.info("Checking if on login page. Result: {}", isOnPage);
        return isOnPage;
    }

    /**
     * Checks if the user is on the Checkout page.
     *
     * @return True if on the Checkout page, otherwise false.
     */
    public boolean isOnCheckOutPage() {
        String currentUrl = driver.getCurrentUrl();
        boolean isOnPage = currentUrl.contains("https://www.saucedemo.com/checkout-step-one.html");
        logger.info("Checking if on login page. Result: {}", isOnPage);
        return isOnPage;
    }
}







