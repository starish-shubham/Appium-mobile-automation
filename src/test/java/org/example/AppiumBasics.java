package org.example;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.example.apiDemos.apiDemosPageSteps;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

public class AppiumBasics {

    @Test
    public void appiumTest() throws Exception {

        // Start Appium Server Programmatically
//        AppiumDriverLocalService service =
//                new AppiumServiceBuilder()
//                        .withAppiumJS(new File("C:\\Users\\Shubham\\AppData\\Roaming\\npm\\node_modules\\appium\\lib\\main.js"))
//                        .withIPAddress("127.0.0.1")
//                        .usingPort(4723)
//                        .build();
//
//        service.start();

        System.out.println("Appium Server Started Successfully!");

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

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
//        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:5037"), options);

        // Create instance of apiDemosPageSteps
        apiDemosPageSteps apiDemos = new apiDemosPageSteps(driver);

        System.out.println("App launched successfully on real device!");
        // Add automation steps here

//        apiDemos.openPreference("Preference");
//        apiDemos.openPreference();
        driver.findElement(AppiumBy.accessibilityId("Preference")).click();
        driver.findElement(AppiumBy.accessibilityId("Preference")).click();




        driver.quit();
//        service.stop();
    }
}
