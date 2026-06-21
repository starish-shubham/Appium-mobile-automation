package org.example;

import org.example.client.apiDemos.apiDemosPageSteps;
import org.openqa.selenium.ScreenOrientation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class apiDemosTest extends BaseTest{

    private apiDemosPageSteps apiDemo;
    private static final Logger log = LoggerFactory.getLogger(apiDemosTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        System.out.println("BeforeMethod executed");
        apiDemo = new apiDemosPageSteps(getDriver());
        SoftAssert softAssert = new SoftAssert();
    }

    @Test(priority = 1, groups = {"smoke1"})
    public void TestClicks() throws Exception {
        apiDemo.selectMenu("Preference");
        apiDemo.setWiFiPassword("Shubham WiFi", ScreenOrientation.LANDSCAPE);
        softAssert.assertAll();
    }

    @Test(priority = 2, groups = {"smoke"})
    public void LongPressTest() throws Exception {
        apiDemo.longPressOnPeopleNames("People Names", ScreenOrientation.PORTRAIT);
        softAssert.assertAll();
    }

    @Test(priority = 3, groups = {"smoke"})
    public void DragAndDrop() throws Exception {
        apiDemo.dragAndDrop();
        softAssert.assertAll();
    }

    @Test(priority = 4, groups = {"smoke"})
    public void ScrollTest() throws Exception{
        apiDemo.scrollTest( ScreenOrientation.PORTRAIT);
        softAssert.assertAll();
    }

    @Test(priority = 5, groups = {"smoke"})
    public void SwipeTest() throws Exception{
        apiDemo.swipeTest();
        softAssert.assertAll();
    }

}
