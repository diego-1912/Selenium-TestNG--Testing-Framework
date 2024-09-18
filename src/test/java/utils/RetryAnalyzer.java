package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import pages.BaseClass;

public class RetryAnalyzer implements IRetryAnalyzer {

    // Counter to keep track of the number of retries
    private int retryCount = 0;
    // Maximum number of retries allowed
    private static final int maxRetryCount = 2; // Set the maximum retry count

    /**
     * Determines whether the failed test should be retried.
     *
     * @param result The result of the test execution.
     * @return True if the test should be retried, otherwise false.
     */
    @Override
    public boolean retry(ITestResult result) {
        // Check if the current retry count is less than the maximum allowed retries
        if (retryCount < maxRetryCount) {
            retryCount++; // Increment the retry count
            // Reinitialize WebDriver before retrying the test
            BaseClass baseClass = (BaseClass) result.getInstance();
            baseClass.setUp(result.getTestContext().getCurrentXmlTest().getParameter("browser"),
                    result.getTestContext().getCurrentXmlTest().getParameter("baseUrl"));
            return true; // Return true to indicate that the test should be retried
        }
        return false; // Return false to indicate no further retries
    }
}
