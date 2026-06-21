package org.example;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.example.core.utils.EmailUtils;
import org.example.core.utils.ExtentReportManager;
import org.example.core.utils.MobileActions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    // Keep these protected so your actual Test classes can inherit and use them easily
    protected AndroidDriver driver;
    protected MobileActions mobile;
    protected AppiumDriverLocalService service;
    protected SoftAssert softAssert;

    @BeforeSuite(alwaysRun = true)
    public void setupReport() {
        ExtentReportManager.getInstance(); // Initializes the HTML engine
    }

    @BeforeClass(alwaysRun = true)
    public void configureAppium() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Android Device");
//        options.setUdid("ZD2223GKCT");       // Wired Debugging ID
        options.setUdid("192.168.1.10:4455");       // Wireless Debugging ID
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");

        options.setAppPackage("io.appium.android.apis");
        options.setAppActivity("io.appium.android.apis.ApiDemos");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        mobile = new MobileActions(driver);
        System.out.println("Driver initialized = " + driver);
    }

    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {
        // Automatically creates a dedicated log block in Extent Report for each test case
        ExtentReportManager.createTest(method.getName());
        this.softAssert = new SoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult result) {
        if (ExtentReportManager.getTest() != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                // Captures assertions or failures thrown outside MobileActions (e.g., Assert.assertEquals)
                ExtentReportManager.getTest().fail(result.getThrowable());

                // Backup screenshot attachment block for global test assertions
                try {
                    String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    ExtentReportManager.getTest().fail("Test overall execution failed. Captured state:",
                            com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                } catch (Exception e) {
                    System.out.println("Failed to attach test teardown screenshot: " + e.getMessage());
                }

            } else if (result.getStatus() == ITestResult.SKIP) {
                ExtentReportManager.getTest().skip("Test Skipped: " + result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                ExtentReportManager.getTest().pass("Test Passed Successfully");
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownReport() {
        ExtentReportManager.flush(); // Generates the final ExtentReport.html file
        System.out.println("Extent Report successfully written to disk.");
        try {
            EmailUtils.sendEmailWithReport();
        } catch (Exception e) {
            System.out.println("Could not dispatch automation report email: " + e.getMessage());
        }
    }

    // ================= ENHANCED MOBILE GESTURES =================

    public void scrollToEndAction() {
        try {
            boolean canScrollMore;
            do {
                canScrollMore = (Boolean) ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
                        "left", 100, "top", 100, "width", 200, "height", 200,
                        "direction", "down",
                        "percent", 3.0
                ));
            } while (canScrollMore);
        } catch (Exception e) {
            throw new RuntimeException("Failed while performing Scroll To End Gesture", e);
        }
    }


    public static By genericBySelector(String menuName) {
        return AppiumBy.accessibilityId(menuName);
    }

    public AndroidDriver getDriver() {
        return this.driver;
    }

    public MobileActions getMobileActions() {
        return this.mobile;
    }
}