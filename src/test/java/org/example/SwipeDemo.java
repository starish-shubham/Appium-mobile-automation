package org.example;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SwipeDemo extends BaseTest{

    @Test
    public void SwipeDemoTest() throws Exception {

        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Gallery")).click();
        WebElement fisrtImage = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='1. Photos']"));
        fisrtImage.click();
//        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='1. Photos']")).click();
        WebElement el = driver.findElement(AppiumBy.xpath("//android.widget.ImageView[1]"));
        String foccusableFirst = driver.findElement(AppiumBy.xpath("//android.widget.ImageView[1]")).getAttribute("focusable");
        Assert.assertEquals(foccusableFirst, "true");
        //swipe
        String foccusableSecond = driver.findElement(AppiumBy.xpath("//android.widget.ImageView[2]")).getAttribute("focusable");
        Assert.assertEquals(foccusableSecond, "false");
        ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
//                "elementId", ((WebElement) driver.findElement(AppiumBy.xpath("//android.widget.ImageView[1]"))).getAttribute("elementId"),
                "elementId", ((RemoteWebElement) el).getId(),
                "direction", "left",
                "percent", 0.75
        ));
        Thread.sleep(1000);
        String foccusableFirst1 = driver.findElement(AppiumBy.xpath("//android.widget.ImageView[1]")).getAttribute("focusable");
        String foccusableSecond2 = driver.findElement(AppiumBy.xpath("//android.widget.ImageView[2]")).getAttribute("focusable");
        Assert.assertEquals(foccusableFirst1, "false","verify 1st image is not focusable after swipe");
        Assert.assertEquals(foccusableSecond2, "true", "verify 2nd image is focusable after swipe");
//        swipeAction(driver.findElement(AppiumBy.xpath("//android.widget.ImageView[1]")), "left");

        Thread.sleep(2000);


    }
}
