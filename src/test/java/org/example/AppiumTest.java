package org.example;

import io.appium.java_client.AppiumBy;
import org.example.apiDemos.apiDemosPageSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppiumTest extends BaseTest{

    @Test
    public void appiumTest() throws Exception {

        // Create instance of apiDemosPageSteps
//        apiDemosPageSteps apiDemos = new apiDemosPageSteps(driver);

        System.out.println("App launched successfully on real device!");
        // Add automation steps here

//        apiDemos.openPreference("Preference");
//        apiDemos.openPreference();
        driver.findElement(AppiumBy.accessibilityId("Preference")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc='3. Preference dependencies']")).click();
        driver.findElement(AppiumBy.id("android:id/checkbox")).click();
        driver.findElement(AppiumBy.xpath("(//android.widget.RelativeLayout)[2]")).click();
        String alertTitle = driver.findElement(AppiumBy.id("android:id/alertTitle")).getText();
        Assert.assertEquals(alertTitle, "WiFi settings");
        driver.findElement(AppiumBy.id("android:id/edit")).sendKeys("Shubham WiFi");
        driver.findElement(AppiumBy.id("android:id/button1")).click();




    }
}
