package org.example;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class pageFunctions {
    public By a11y(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("AccessibilityId cannot be null or empty");
            }
            return AppiumBy.accessibilityId(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create AccessibilityId locator for: " + id, e);
        }
    }

    public By id(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("ID cannot be null or empty");
            }
            return AppiumBy.id(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ID locator for: " + id, e);
        }
    }

    public By xpath(String xp) {
        try {
            if (xp == null || xp.trim().isEmpty()) {
                throw new IllegalArgumentException("XPath cannot be null or empty");
            }
            return By.xpath(xp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create XPath locator for: " + xp, e);
        }
    }


}
