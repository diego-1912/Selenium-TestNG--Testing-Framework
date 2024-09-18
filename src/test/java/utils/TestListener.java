package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    // ExtentReports instance for generating the report
    private static ExtentReports extent;
    // ThreadLocal to ensure ExtentTest instances are thread-safe
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    // Path where the test report will be generated
    private static String reportPath;

    /**
     * Called when the test suite starts.
     * Initializes the Extent report.
     *
     * @param context The context of the test suite.
     */
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting Test Suite: " + context.getName());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Define the report path using a timestamp to ensure uniqueness
        reportPath = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "ExtentReport_" + timeStamp + ".html";

        // Initialize ExtentSparkReporter for HTML report generation
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        // Configure ExtentSparkReporter
        spark.config().setDocumentTitle("Test Report");
        spark.config().setReportName("Extent Report");

        // Attach reporter to ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(spark);
        System.out.println("Extent report initialized at: " + reportPath);
    }

    /**
     * Called when an individual test starts.
     *
     * @param result The result of the test execution.
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Starting Test: " + result.getName());
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test); // Set the ExtentTest instance to the current thread
    }

    /**
     * Called when a test passes.
     *
     * @param result The result of the test execution.
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.pass("Test passed"); // Log test passed status in the report
        }
    }

    /**
     * Called when a test fails.
     *
     * @param result The result of the test execution.
     */
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());
        ExtentTest test = extentTest.get();

        if (test != null) {
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                test.fail(throwable); // Log the exception
                String exceptionMessage = throwable.getMessage();
                test.fail("Exception Message: " + exceptionMessage);
                String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(throwable);
                test.fail("Stack Trace: " + stackTrace);
            } else {
                test.fail("Test failed without throwing an exception");
            }
        } else {
            System.err.println("Failed to log test result; ExtentTest instance is null.");
        }

        // Capture screenshot for the failed test
        captureScreenshot(result);
    }

    /**
     * Called when a test is skipped.
     *
     * @param result The result of the test execution.
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.skip(result.getThrowable()); // Log skipped test in the report
        }
    }

    /**
     * Called when a test fails but is within the success percentage.
     *
     * @param result The result of the test execution.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Failed Within Success Percentage: " + result.getName());
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.warning("Test failed but within success percentage");
        }
    }

    /**
     * Called when the test suite finishes.
     *
     * @param context The context of the test suite.
     */
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Ending Test Suite: " + context.getName());
        try {
            if (extent != null) {
                extent.flush(); // Write the test results to the report
                System.out.println("Extent report generated successfully at: " + reportPath);
            }
        } catch (Exception e) {
            System.err.println("Failed to generate Extent report: " + e.getMessage());
            e.printStackTrace();
        } finally {
            extentTest.remove(); // Clean up ThreadLocal to prevent memory leaks
        }
    }

    /**
     * Captures a screenshot on test failure.
     *
     * @param result The result of the test execution.
     */
    private void captureScreenshot(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = null;

        // Retrieve the WebDriver instance from the test class
        try {
            driver = (WebDriver) testClass.getClass().getField("driver").get(testClass);
        } catch (Exception e) {
            System.err.println("Failed to get WebDriver instance: " + e.getMessage());
            return;
        }

        // Check if the driver supports taking screenshots
        if (driver != null && driver instanceof TakesScreenshot) {
            String screenshotPath = "screenshots" + File.separator + result.getName() + "_" + System.currentTimeMillis() + ".png";
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                org.apache.commons.io.FileUtils.copyFile(screenshot, new File(screenshotPath));
                ExtentTest test = extentTest.get();
                if (test != null) {
                    // Attach the screenshot to the test report
                    test.fail("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            } catch (IOException e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
}
