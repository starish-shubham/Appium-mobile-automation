package org.example;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    public AndroidDriver driver;
    public AppiumDriverLocalService service;

    @BeforeClass
    public void ConfigureAppium() throws MalformedURLException {
        // Common Appium configuration can be placed here
        // Start Appium Server Programmatically
//        service =
//                new AppiumServiceBuilder()
//                        .withAppiumJS(new File("C:\\Users\\Shubham\\AppData\\Roaming\\npm\\node_modules\\appium\\lib\\main.js"))
//                        .withIPAddress("127.0.0.1")
//                        .usingPort(4723)
//                        .build();
//
//        service.start();

        // Appium Options
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Android Device");   // Any name
//        options.setUdid("192.168.1.6:5037");       // Wireless Debugging ID (ADB connect)
        options.setUdid("192.168.1.6:5555");       // Wireless Debugging ID (ADB connect)
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");

        // Launch Installed App
        options.setAppPackage("io.appium.android.apis");
        options.setAppActivity("io.appium.android.apis.ApiDemos");

        // If NOT installed, provide APK:
        // options.setApp("C:\\path\\to\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
//        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:5037"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    public void longPressAction(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId()
        ));
    }

    @AfterClass
    public void tearDown() {
        // Common teardown steps can be placed here
        driver.quit();
//        service.stop();
    }
}
