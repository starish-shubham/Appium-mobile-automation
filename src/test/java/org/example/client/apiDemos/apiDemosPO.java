package org.example.client.apiDemos;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class apiDemosPO {
    public static By Accessibility1 = AppiumBy.accessibilityId("Access'ibility");
    public static By Accessibility2 = AppiumBy.accessibilityId("Accessibility");
    public static By Animation = AppiumBy.accessibilityId("Animation");
    public static By App = AppiumBy.accessibilityId("App");
    public static By Content = AppiumBy.accessibilityId("Content");
    public static By Graphics = AppiumBy.accessibilityId("Graphics");
    public static By Media = AppiumBy.accessibilityId("Media");
    public static By NFC = AppiumBy.accessibilityId("NFC");
    public static By OS = AppiumBy.accessibilityId("OS");
    public static By preference = AppiumBy.accessibilityId("Preference");
    public static By Text = AppiumBy.accessibilityId("Text");
    public static By Views = AppiumBy.accessibilityId("Views");
    public static String prederenceDependencies = "//android.widget.TextView[@content-desc='3. Preference dependencies']";
    public static By dragAndDrop = AppiumBy.accessibilityId("Drag and Drop");
    public static By imageView = AppiumBy.xpath("//android.widget.ImageView[1]");
    public static By image2 = AppiumBy.xpath("//android.widget.ImageView[2]");
    public static By genericMenuOption(String menuName) {
        return AppiumBy.accessibilityId(menuName);
    }
}
