package org.example.core.utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.flutter.commands.ScrollParameter;
import io.appium.java_client.remote.SupportsRotation;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MobileActions {

    private static final Logger log = LoggerFactory.getLogger(MobileActions.class);

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    public enum SwipeDirection {
        UP, DOWN, LEFT, RIGHT
    }
    public enum NavigationKey {
        HOME, BACK
    }

    public MobileActions(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ================= CLICK =================

    public void click(By locator) {
        try {
            log.info("Clicking element: {}", locator);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            log.info("Successfully clicked: {}", locator);
        } catch (TimeoutException e) {
            captureScreenshot("click_timeout_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Element not clickable: {}", locator, e);
            throw new RuntimeException("Element not clickable: " + locator, e);
        } catch (NoSuchElementException e) {
            captureScreenshot("click_notfound_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Element not found: {}", locator, e);
            throw new RuntimeException("Element not found: " + locator, e);
        } catch (Exception e) {
            captureScreenshot("click_error_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Unexpected error while clicking {}", locator, e);
            throw new RuntimeException("Failed to click element: " + locator, e);
        }
    }

    public void clickByAccessibilityId(String value) {
        click(AppiumBy.accessibilityId(value));
    }

    public void clickByXpath(String xpath) {
        click(By.xpath(xpath));
    }

    public void clickById(String id) {
        click(By.id(id));
    }

    public void pressNavigateKey(NavigationKey key) {
        StepLogger.logStep("Pressing device hardware navigation button: " + key.name());

        try {
            switch (key) {
                case BACK:
                    Log.info("Executing system back navigation command.");
                    driver.navigate().back();
                    break;

                case HOME:
                    Log.info("Executing native Android Home hardware key event mapping.");
                    if (driver instanceof AndroidDriver) {
                        // Cast driver to Android to send a raw system hardware keycode event
                        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME));
                    } else {
                        // iOS Fallback simulation if running cross-platform architecture
                        throw new WebDriverException("Native hardware HOME key simulation is exclusively supported on Android OS driver instances.");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported device navigation button requested: " + key);
            }
            Log.info("Hardware navigation action " + key.name() + " acknowledged successfully.");

        } catch (Exception e) {
            // Reusing your custom screenshot capture method safely
            captureScreenshot("pressNavigateKey_error_" + key.name().toLowerCase());
            log.error("System hardware key event execution crashed...", e);
            throw new RuntimeException("Unable to process system interaction button event for: " + key.name(), e);
        }
    }

    // ================= SEND KEYS =================

    public void type(By locator, String text) {
        try {
            log.info("Typing '{}' into {}", text, locator);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            log.info("Successfully entered text");
        } catch (Exception e) {
            captureScreenshot("type_error_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Unable to enter text '{}' into {}", text, locator, e);
            throw new RuntimeException("Failed to enter text into " + locator, e);
        }
    }

    public void typeByAccessibilityId(String value, String text) {
        type(AppiumBy.accessibilityId(value), text);
    }

    public void typeByXpath(String xpath, String text) {
        type(By.xpath(xpath), text);
    }

    public void typeById(String id, String text) {
        type(By.id(id), text);
    }

    // ================= GET TEXT =================

    public String getText(By locator) {
        try {
            log.info("Getting text from {}", locator);
            String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
            log.info("Text found: {}", text);
            return text;
        } catch (Exception e) {
            captureScreenshot("getText_error_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Unable to get text from {}", locator, e);
            throw new RuntimeException("Unable to get text from " + locator, e);
        }
    }

    public void longPressAction(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) element).getId()
            ));
        } catch (Exception e) {
            captureScreenshot("longPressAction_error");
            log.error("Failed to perform long press action", e);
            throw new RuntimeException("Failed to perform long press action", e);
        }
    }

    public String getToastMessage() {
        try {
            By toastLocator = By.xpath("//android.widget.Toast");
            String toastText = wait.until(ExpectedConditions.presenceOfElementLocated(toastLocator)).getAttribute("name");
            log.info("Toast message captured: {}", toastText);
            return toastText;
        } catch (Exception e) {
            captureScreenshot("getToastMessage_error");
            log.error("Unable to capture toast message", e);
            throw new RuntimeException("Failed to get toast message", e);
        }
    }

    // ================= DISPLAYED =================

    public boolean isDisplayed(By locator) {
        try {
            boolean result = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
            log.info("Element {} displayed status = {}", locator, result);
            return result;
        } catch (Exception e) {
            // No screenshot here typically, as an element missing can be a normal test assertion path
            log.warn("Element {} not displayed", locator);
            return false;
        }
    }

    // ================= FIND ELEMENT =================

    public WebElement findElement(By locator) {
        try {
            log.info("Finding element {}", locator);
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            captureScreenshot("findElement_error_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Unable to find element {}", locator, e);
            throw new RuntimeException("Element not found: " + locator, e);
        }
    }

    // ================= FIND ELEMENTS =================

    public List<WebElement> findElements(By locator) {
        try {
            log.info("Finding elements {}", locator);
            return driver.findElements(locator);
        } catch (Exception e) {
            captureScreenshot("findElements_error_" + locator.toString().replaceAll("[^a-zA-Z0-9]", "_"));
            log.error("Unable to find elements {}", locator, e);
            throw new RuntimeException("Elements not found: " + locator, e);
        }
    }

    // ================= WAIT =================

    public void implicitWait(int seconds) {
        try {
            log.info("Applying implicit wait of {} seconds", seconds);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        } catch (Exception e) {
            log.error("Unable to apply implicit wait", e);
            throw new RuntimeException("Failed to set implicit wait", e);
        }
    }

    public void dragAndDropAction(WebElement element, int endX, int endY) {
        try {
            ((JavascriptExecutor) driver).executeScript("mobile: dragGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) element).getId(),
                    "endX", endX,
                    "endY", endY
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed while performing Drag and Drop Gesture", e);
        }
    }

    private boolean executeScroll(ScrollParameter.ScrollDirection direction) {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX, startY, endX, endY;

            switch (direction) {
                case DOWN:
                    startX = endX = size.width / 2;
                    startY = (int) (size.height * 0.9);
                    endY = (int) (size.height * 0.1);
                    break;
                case UP:
                    startX = endX = size.width / 2;
                    startY = (int) (size.height * 0.1);
                    endY = (int) (size.height * 0.9);
                    break;
                case RIGHT:
                    startY = endY = size.height / 2;
                    startX = (int) (size.width * 0.9);
                    endX = (int) (size.width * 0.1);
                    break;
                case LEFT:
                    startY = endY = size.height / 2;
                    startX = (int) (size.width * 0.1);
                    endX = (int) (size.width * 0.9);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid scroll direction");
            }
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1);
            scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scroll.addAction(finger.createPointerMove(Duration.ofMillis(300), PointerInput.Origin.viewport(), endX, endY));
            scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(scroll));
            return true;

        } catch (WebDriverException e) {
            System.err.println("Screen interaction failed while attempting to scroll " + direction + ": " + e.getMessage());
            captureScreenshot("scroll_failure_" + direction.name().toLowerCase());
            return false;
        }
    }

    public boolean scrollDown() {
        return executeScroll(ScrollParameter.ScrollDirection.DOWN);
    }

    public boolean scrollUp() {
        return executeScroll(ScrollParameter.ScrollDirection.UP);
    }

    public boolean scrollLeft() {
        return executeScroll(ScrollParameter.ScrollDirection.LEFT);
    }

    public boolean scrollRight() {
        return executeScroll(ScrollParameter.ScrollDirection.RIGHT);
    }

    /**
     * Dynamically scrolls down looking for an element.
     * Captures a screenshot if the loop ends and the element is never found.
     */
    public void scrollTillLocator(By locator, int maxScrolls) {
        int attempt = 0;

        while (attempt < maxScrolls) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    System.out.println("Element successfully located and visible.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Element not found on screen yet. Scrolling down... (Attempt " + (attempt + 1) + "/" + maxScrolls + ")");
            } catch (StaleElementReferenceException staleEx) {
                System.out.println("DOM updated midway. Re-detecting element on next iteration.");
            }

            // Perform the scroll down. If the W3C action itself crashes, it handles its own screenshot inside executeScroll.
            if (!scrollDown()) {
                throw new WebDriverException("Aborting search: Appium failed to execute physical scroll action.");
            }
            attempt++;
        }
    }

    public String getAttribute(By locator, String attributeType) {
        // High-level human readable report tracking
        StepLogger.logStep("Fetching attribute '" + attributeType + "' from locator: " + locator.toString());

        try {
            // Find the element first
            WebElement element = driver.findElement(locator);

            // Fetch the attribute
            String value = element.getAttribute(attributeType);

            // Log the technical details for debugging
            Log.info("Successfully retrieved attribute '" + attributeType + "' = [" + value + "]");
            return value;

        } catch (NoSuchElementException e) {
            Log.error("Failed to get attribute '" + attributeType + "'. Element could not be found with locator: " + locator.toString());
            return ""; // Return empty string or handle according to your assertions framework

        } catch (StaleElementReferenceException e) {
            Log.warn("Element went stale while reading attribute. Retrying discovery once...");
            try {
                // Quick retry if the DOM updated mid-execution
                String value = driver.findElement(locator).getAttribute(attributeType);
                Log.info("Retry successful. Attribute value = [" + value + "]");
                return value;
            } catch (Exception retryEx) {
                Log.error("Retry failed to read attribute due to stale DOM: " + retryEx.getMessage());
                return "";
            }

        } catch (WebDriverException e) {
            Log.error("Appium driver encountered an error while pulling attribute property: " + e.getMessage());
            return "";
        }
    }

    public void swipe(By locator, SwipeDirection direction) {
        StepLogger.logStep("Performing swipe " + direction + " on element: " + locator.toString());

        try {
            // 1. Locate the target element
            WebElement container = driver.findElement(locator);

            // 2. Extract bounding box dimensions of the target container
            Rectangle rect = container.getRect();
            int startX, startY, endX, endY;

            // Calculate anchor coordinates strictly bounded inside the target element
            switch (direction) {
                case DOWN: // Swiping down moves content down (drag from top to bottom)
                    startX = endX = rect.getX() + (rect.getWidth() / 2);
                    startY = (int) (rect.getY() + (rect.getHeight() * 0.2));
                    endY = (int) (rect.getY() + (rect.getHeight() * 0.8));
                    break;
                case UP: // Swiping up moves content up (drag from bottom to top)
                    startX = endX = rect.getX() + (rect.getWidth() / 2);
                    startY = (int) (rect.getY() + (rect.getHeight() * 0.8));
                    endY = (int) (rect.getY() + (rect.getHeight() * 0.2));
                    break;
                case RIGHT: // Swiping right moves content right (drag left to right)
                    startY = endY = rect.getY() + (rect.getHeight() / 2);
                    startX = (int) (rect.getX() + (rect.getWidth() * 0.2));
                    endX = (int) (rect.getX() + (rect.getWidth() * 0.8));
                    break;
                case LEFT: // Swiping left moves content left (drag right to left)
                    startY = endY = rect.getY() + (rect.getHeight() / 2);
                    startX = (int) (rect.getX() + (rect.getWidth() * 0.8));
                    endX = (int) (rect.getX() + (rect.getWidth() * 0.2));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid swipe direction provided.");
            }

            // 3. Build W3C Pointer Input Actions Sequence
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipeSeq = new Sequence(finger, 1);

            swipeSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipeSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipeSeq.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), endX, endY));
            swipeSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // 4. Perform gesture
            driver.perform(Collections.singletonList(swipeSeq));
            Log.info("Swipe " + direction + " executed successfully within container boundaries.");

        } catch (NoSuchElementException e) {
            String errorMsg = "Swipe failed: Element container not found with locator: " + locator;
            Log.error(errorMsg);
            captureScreenshot("no_such_element_" + direction.name().toLowerCase());
            throw e;

        } catch (StaleElementReferenceException e) {
            String errorMsg = "Swipe failed: Target container element went stale mid-interaction.";
            Log.error(errorMsg);
            captureScreenshot("stale_element_" + direction.name().toLowerCase());
            throw e;

        } catch (WebDriverException e) {
            String errorMsg = "Swipe failed: Driver encountered an engineering exception during touch sequence: " + e.getMessage();
            Log.error(errorMsg);
            captureScreenshot("driver_gesture_fault_" + direction.name().toLowerCase());
            throw e;
        }
    }

    public void rotateDevice(ScreenOrientation orientation) {
        StepLogger.logStep("Attempting to rotate device display orientation to: " + orientation.name());

        // Check if the current driver instance supports screen rotation API hooks
        if (!(driver instanceof SupportsRotation)) {
            String msg = "The underlying Appium driver instance configuration does not support rotation capabilities.";
            Log.error(msg);
            captureScreenshot("unsupported_rotation_capability");
            throw new WebDriverException(msg);
        }

        try {
            // Cast driver to access rotation capabilities natively
            SupportsRotation rotationDriver = (SupportsRotation) driver;

            // Check current orientation to avoid redundant automation commands
            if (rotationDriver.getOrientation() == orientation) {
                Log.info("Device is already configured in " + orientation.name() + " mode. Skipping command.");
                return;
            }

            // Execute physical hardware rotation command
            rotationDriver.rotate(orientation);
            Log.info("Rotation command acknowledged by OS. Setting orientation to " + orientation.name());

            // CRITICAL: Give the mobile OS window management subsystem a brief moment (1.5s)
            // to stabilize rendering elements in the new aspect ratio dimensions.
            Thread.sleep(1500);
            Log.info("Device viewport successfully stabilized in " + orientation.name() + " layout mode.");

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            Log.warn("Settle delay thread was interrupted during rotation transition: " + ie.getMessage());

        } catch (WebDriverException e) {
            String errorMsg = "Hardware screen rotation failed! The current view might be locked or application layout crashes on orientation change. Error: " + e.getMessage();
            Log.error(errorMsg);
            captureScreenshot("rotation_fault_" + orientation.name().toLowerCase());
            throw e;
        }
    }

    public void setSystemClipboardText(String textToCopy) {
        StepLogger.logStep("Setting text to system clipboard: " + textToCopy);

        if (driver instanceof HasClipboard) {
            try {
                ((HasClipboard) driver).setClipboardText(textToCopy);
                Log.info("Text payload successfully pushed into system memory.");
            } catch (WebDriverException e) {
                Log.error("Driver failed to update system clipboard hardware state: " + e.getMessage());
            }
        }
    }

    public String getSystemClipboardText() {
        StepLogger.logStep("Retrieving text payload from the system clipboard registry.");

        if (!(driver instanceof HasClipboard)) {
            Log.error("The current driver capability configuration does not support native clipboard interactions.");
            return "";
        }

        HasClipboard clipboardDriver = (HasClipboard) driver;

        try {
            // Fetch clipboard data
            String text = clipboardDriver.getClipboardText();
            Log.info("Clipboard payload successfully retrieved: [" + text + "]");
            return text;

        } catch (WebDriverException e) {
            Log.error("System security policy or driver exception blocked reading from clipboard: " + e.getMessage());
            return "";
        }
    }

    // ================= SCREENSHOT UTILITY (FIXED VIA BASE64) =================

    private void captureScreenshot(String actionName) {
        try {
            if (driver == null) {
                log.warn("Driver is null; cannot take screenshot.");
                return;
            }

            // 1. Capture the screenshot directly as a Base64 string
            String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

            // 2. Attach it safely to the Extent Report without file paths
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().fail("Action failed: " + actionName,
                        com.aventstack.extentreports.MediaEntityBuilder
                                .createScreenCaptureFromBase64String(base64Screenshot)
                                .build());
            }

            log.info("Failure screenshot successfully embedded as Base64 string for action: {}", actionName);

        } catch (WebDriverException e) {
            log.error("WebDriver exception occurred while taking Base64 screenshot", e);
        } catch (Exception e) {
            log.error("Unexpected error while capturing screenshot", e);
        }
    }

}