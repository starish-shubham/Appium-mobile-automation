package org.example.client.apiDemos;

import io.appium.java_client.AppiumBy;
import org.example.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DragDrop extends BaseTest {

    @Test
    public void dragDrop() throws Exception {
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.accessibilityId("Drag and Drop")).click();
        WebElement source = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_1"));
        WebElement destination = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_2"));
//        dragAndDropAction(source, 413, 364);
        Thread.sleep(2000);
        String result = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_result_text")).getText();
        Assert.assertEquals(result, "Dropped!");
    }
}
