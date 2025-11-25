package org.example.apiDemos;

import io.appium.java_client.AppiumDriver;

import java.text.MessageFormat;

public class apiDemosPageSteps {
    AppiumDriver driver;
    // Constructor to pass driver from main class
    public apiDemosPageSteps(AppiumDriver driver) {
        this.driver = driver;
    }

    public void openPreference(String menuName) {
//        driver.findElement(apiDemosPageObjects.preference).click();
    }


}
