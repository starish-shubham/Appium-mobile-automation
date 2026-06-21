package org.example.client.apiDemos;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.example.core.utils.MobileActions;
import org.example.core.utils.StepLogger;
import org.example.core.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

import static org.testng.Assert.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class apiDemosPageSteps{
    private AppiumDriver driver;
    private MobileActions mobile;
//    private static final Logger log = LoggerFactory.getLogger(apiDemosPageSteps.class);

    // Constructor to pass driver from main class
    public apiDemosPageSteps(AppiumDriver driver) {
        System.out.println("Constructor called");
        System.out.println("Driver received = " + driver);
        this.driver = driver;
        this.mobile = new MobileActions(driver);
        PageFactory.initElements(driver, this);
    }

//    @Description("This method clicks on the menu option in API Demos home page")
    public void openPreference(String menuName) {
        mobile.click(apiDemosPO.genericMenuOption(menuName));
    }

//    @Description("This method selects the menu option in Preference and verifies the menu text")
    public void selectMenu(String menuName) {
        String menuText = mobile.getText(apiDemosPO.genericMenuOption(menuName));
        assertEquals(menuName, menuText, "Menu Text in Preference");
        mobile.click(apiDemosPO.genericMenuOption(menuName));
    }

//    @Description("This method sets the WiFi password in the popup and verifies the title of the popup")
    public void setWiFiPassword(String wiFiPassword,ScreenOrientation orientation) throws Exception {
        mobile.click(By.xpath("//android.widget.TextView[@content-desc='3. Preference dependencies']"));
        mobile.click(By.id("android:id/checkbox"));
        mobile.rotateDevice(orientation);
        mobile.click(By.xpath("(//android.widget.RelativeLayout)[2]"));
        String alertTitle1 = mobile.getText(By.id("android:id/alertTitle"));
        Assert.assertEquals(alertTitle1, "WiFi settings","Title of popup");
        mobile.setSystemClipboardText(wiFiPassword);
        mobile.type(By.id("android:id/edit"), mobile.getSystemClipboardText());
        mobile.click(By.id("android:id/button1"));
        mobile.pressNavigateKey(MobileActions.NavigationKey.BACK);
        mobile.pressNavigateKey(MobileActions.NavigationKey.BACK);
    }

//    @Description("This method performs long press action on People Names and verifies the options in the context menu")
    public void longPressOnPeopleNames(String name,ScreenOrientation orientation) throws Exception {
        mobile.click(AppiumBy.accessibilityId("Views"));
        mobile.rotateDevice(orientation);
        mobile.click(AppiumBy.accessibilityId("Expandable Lists"));
        mobile.click(AppiumBy.accessibilityId("1. Custom Adapter"));
        WebElement person = mobile.findElement(By.xpath("//android.widget.TextView[@text='" + name + "']"));
        mobile.longPressAction(person);
        List<WebElement> actionList = mobile.findElements(By.className("android.widget.TextView"));
        for(WebElement element : actionList) {
            if(element.getText().equals("Sample action")) {
                element.click();
                String toastMessage = mobile.getToastMessage();
                System.out.println("Toast message after clicking Sample action: " + toastMessage);
            }else {
                assertEquals(element.getText(), "Sample menu", "Verify Sample menu is present in long press options");
            }
        }
        mobile.pressNavigateKey(MobileActions.NavigationKey.BACK);
        mobile.pressNavigateKey(MobileActions.NavigationKey.BACK);
        mobile.pressNavigateKey(MobileActions.NavigationKey.BACK);
    }

    public void dragAndDrop() throws Exception {
        mobile.click(AppiumBy.accessibilityId("Views"));
        mobile.click(apiDemosPO.dragAndDrop);
        WebElement source = mobile.findElement(By.id("io.appium.android.apis:id/drag_dot_1"));
        WebElement destination = mobile.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_2"));
        mobile.dragAndDropAction(source, 413, 364);
        Thread.sleep(2000);
        String result = mobile.getText(AppiumBy.id("io.appium.android.apis:id/drag_result_text"));
        Assert.assertEquals(result, "Dropped!");
    }

    public void scrollTest(ScreenOrientation orientation) throws Exception {
        StepLogger.logStep("Scroll Test");
        mobile.rotateDevice(orientation);
        mobile.click(AppiumBy.accessibilityId("Views"));
        Log.info("Initiating physical Scroll Down action on device screen.");
        mobile.scrollDown();
        boolean isWebView3 = mobile.isDisplayed(AppiumBy.accessibilityId("WebView3"));
        assertEquals(isWebView3, true, "Verify WebView3 is displayed after scrolling to bottom");
        Log.info("Initiating physical Scroll UP action on device screen.");
        mobile.scrollUp();
        boolean isAnimation = mobile.isDisplayed(AppiumBy.accessibilityId("Animation"));
        assertEquals(isAnimation, true, "Verify Animation is displayed after scrolling up");
        Log.info("Scrolling Up till WebView2.");
        mobile.scrollTillLocator(AppiumBy.accessibilityId("WebView2"),2);
        boolean isWebView2 = mobile.isDisplayed(AppiumBy.accessibilityId("WebView2"));
        assertEquals(isWebView2, true, "Verify WebView2 is displayed after scrolling till locator");
        mobile.pressNavigateKey(MobileActions.NavigationKey.BACK);
    }

    public void swipeTest() throws Exception {
        StepLogger.logStep("Swipe Test");
        mobile.click(AppiumBy.accessibilityId("Views"));
        mobile.click(AppiumBy.accessibilityId("Gallery"));
        WebElement firstImage = mobile.findElement(By.xpath("//android.widget.TextView[@text='1. Photos']"));
        firstImage.click();
        String focusableFirst = mobile.getAttribute(apiDemosPO.imageView, "focusable");
        Assert.assertEquals(focusableFirst, "true", "Verify first image is focusable before swipe");
        //swipe
        String foccusableSecond = mobile.getAttribute(By.xpath("//android.widget.ImageView[2]"), "focusable");
        Assert.assertEquals(foccusableSecond, "false", "Verify second image is not focusable before swipe");
        mobile.swipe(apiDemosPO.imageView,MobileActions.SwipeDirection.LEFT);;
        Thread.sleep(1000);
        String focusableFirstAfterSwipe = mobile.getAttribute(apiDemosPO.imageView, "focusable");
        String focusableSecondAfterSwipe = mobile.getAttribute(By.xpath("//android.widget.ImageView[2]"), "focusable");
        Assert.assertEquals(focusableFirstAfterSwipe, "false", "verify 2nd image is focusable after swipe");
        Assert.assertEquals(focusableSecondAfterSwipe, "true", "verify 2nd image is focusable after swipe");
        Thread.sleep(2000);
    }
}
