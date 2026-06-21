package org.example;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ScrollDemo extends BaseTest{

    @Test
    public void ScrollDemoTest() throws Exception {

        driver.findElement(AppiumBy.accessibilityId("Views")).click();

        //Scroll to specific element
//        boolean canScrollMore;
//        do {
////            canScrollMore= (Boolean) ((JavascriptExecutor) driver).executeScript("mobile: flingGesture", ImmutableMap.of(
////                    "elementId", ((RemoteWebElement) driver.findElement(AppiumBy.accessibilityId("WebView"))).getId(),
////                    "direction", "down",
////                    "speed", 500
////            ));
//        }while(canScrollMore);
        scrollToEndAction();
//        driver.findElement(AppiumBy.accessibilityId("WebUI")).click();


        //If don't know how much to scroll
//        driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"WebView\"));"));
        Thread.sleep(2000);


    }
}
