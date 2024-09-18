package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import utils.DriverFactory;
import utils.LogDirectoryInitializer;

public abstract class BaseClass {

    // WebDriver and WebDriverWait for managing browser interactions and explicit waits
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static String baseUrl;
    protected static Logger logger;

    // Constructor for the base class
    public BaseClass() {
    }

    /**
     * Sets up the test environment before the class is executed.
     * Initializes WebDriver, logger, and opens the base URL.
     */
    @Parameters({"browser", "baseUrl"})
    @BeforeClass
    public void setUpClass(String browser, String baseUrl) {
        LogDirectoryInitializer.initializeLogDirectories(); // Initialize log directories
        initializeLogger(browser); // Initialize logger for the specified browser
        System.setProperty("log4j2.debug", "true"); // Enable Log4j2 debugging
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        logger.info("Setting up WebDriver for browser: {}", browser);

        // Initialize WebDriver for the specified browser
        driver = DriverFactory.getDriver(browser);
        BaseClass.baseUrl = baseUrl;

        // Configure browser settings
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Navigate to the base URL and wait for the page to load
        driver.get(BaseClass.baseUrl);
        logger.info("Navigated to base URL: {}", BaseClass.baseUrl);

        waitForPageToLoad();
    }

    /**
     * Initializes the logger based on the specified browser.
     *
     * @param browser The name of the browser for which the logger is initialized.
     */
    private void initializeLogger(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                logger = LoggerFactory.getLogger("ChromeLogger");
                break;
            case "firefox":
                logger = LoggerFactory.getLogger("FirefoxLogger");
                break;
            case "edge":
                logger = LoggerFactory.getLogger("EdgeLogger");
                break;
            default:
                logger = LoggerFactory.getLogger(BaseClass.class);
                break;
        }
    }

    /**
     * Waits until the page is fully loaded by checking the document's ready state.
     */
    private void waitForPageToLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        logger.info("Page fully loaded");
    }

    /**
     * Waits for a web element to be visible on the page.
     *
     * @param locator The By locator of the web element.
     * @return The visible web element.
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Clicks on a web element after ensuring it is clickable.
     *
     * @param locator The By locator of the web element to be clicked.
     */
    protected void click(By locator) {
        waitForElementClickable(locator).click();
        logger.info("Clicked element: {}", locator);
    }

    /**
     * Sends text to a web element after clearing any existing text.
     *
     * @param locator The By locator of the web element.
     * @param text    The text to send to the web element.
     */
    protected void sendKeys(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.info("Entered text '{}' into element: {}", text, locator);
    }

    /**
     * Retrieves the text of a web element.
     *
     * @param locator The By locator of the web element.
     * @return The text of the web element.
     */
    protected String getText(By locator) {
        String text = waitForElementVisible(locator).getText();
        logger.info("Retrieved text '{}' from element: {}", text, locator);
        return text;
    }

    /**
     * Checks if a web element is present on the page.
     *
     * @param locator The By locator of the web element.
     * @return True if the element is present, otherwise false.
     */
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            logger.info("Element is present: {}", locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.info("Element is not present: {}", locator);
            return false;
        }
    }

    /**
     * Selects an option from a dropdown by visible text.
     *
     * @param locator The By locator of the dropdown element.
     * @param text    The visible text of the option to select.
     */
    protected void selectByVisibleText(By locator, String text) {
        Select select = new Select(waitForElementVisible(locator));
        select.selectByVisibleText(text);
        logger.info("Selected '{}' from dropdown: {}", text, locator);
    }

    /**
     * Retrieves the current URL of the browser.
     *
     * @return The current URL.
     */
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: {}", url);
        return url;
    }

    /**
     * Waits for a web element to be clickable.
     *
     * @param locator The By locator of the web element.
     * @return The clickable web element.
     */
    public WebElement waitForElementClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for a web element to be visible.
     *
     * @param locator The By locator of the web element.
     * @return The visible web element.
     */
    public WebElement waitForElementVisibility(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Retrieves the text of a web element after waiting for its visibility.
     *
     * @param locator The By locator of the web element.
     * @return The text of the web element.
     */
    public String getElementText(By locator) {
        WebElement element = waitForElementVisibility(locator);
        return element.getText();
    }

    /**
     * Closes the WebDriver after all tests have been executed.
     */
    @AfterClass
    public void tearDownClass() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error occurred while closing the browser: {}", e.getMessage());
            }
        }
    }

    /**
     * Sets up the test environment before each test method.
     * Navigates to the base URL to ensure a fresh start for each test.
     */
    @BeforeMethod
    public void setUpMethod() {
        driver.get(baseUrl);
        logger.info("Navigated to base URL before test method: {}", baseUrl);
    }

    // Placeholder for future setup implementation
    public void setUp(String browser, String baseUrl) {
    }
}
