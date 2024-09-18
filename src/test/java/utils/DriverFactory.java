package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

    /**
     * Returns a WebDriver instance based on the specified browser type.
     *
     * @param browser The name of the browser (e.g., "chrome", "firefox", "edge").
     * @return The WebDriver instance for the specified browser.
     */
    public static WebDriver getDriver(String browser) {
        // Determine which browser to use based on the provided parameter
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup(); // Set up the ChromeDriver using WebDriverManager
                ChromeOptions chromeOptions = new ChromeOptions();
                // Add Chrome-specific options
                chromeOptions.addArguments("--headless"); // Run Chrome in headless mode
                chromeOptions.addArguments("--incognito"); // Run Chrome in incognito mode
                return new ChromeDriver(chromeOptions); // Return a new instance of ChromeDriver

            case "firefox":
                WebDriverManager.firefoxdriver().setup(); // Set up the FirefoxDriver using WebDriverManager
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                // Add Firefox-specific options
                firefoxOptions.addArguments("-private"); // Run Firefox in private mode
                firefoxOptions.addArguments("--headless"); // Run Firefox in headless mode
                return new FirefoxDriver(firefoxOptions); // Return a new instance of FirefoxDriver

            case "edge":
                WebDriverManager.edgedriver().setup(); // Set up the EdgeDriver using WebDriverManager
                EdgeOptions edgeOptions = new EdgeOptions();
                // Add Edge-specific options
                edgeOptions.addArguments("--inprivate"); // Run Edge in private mode
                edgeOptions.addArguments("--headless"); // Run Edge in headless mode
                return new EdgeDriver(edgeOptions); // Return a new instance of EdgeDriver

            default:
                // Throw an exception if the browser is not supported
                throw new IllegalArgumentException("Browser " + browser + " not supported.");
        }
    }
}
