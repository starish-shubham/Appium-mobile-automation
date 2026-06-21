package org.example.core.utils;

import com.aventstack.extentreports.Status;
import org.testng.Reporter;
// Import your manager class
import org.example.core.utils.ExtentReportManager;

public class StepLogger {

    /**
     * Highlights business-level steps in your execution and routes them
     * to Console, TestNG Reporter, and Extent Reports.
     */
    public static void logStep(String stepDescription) {
        String formattedStep = "=== STEP: " + stepDescription + " ===";

        // 1. Log to Console
        System.out.println(formattedStep);

        // 2. Log to TestNG's HTML reports
        Reporter.log(formattedStep + "<br>");

        // 3. Log to Extent Report dynamically via your custom ExtentReportManager
        // Fixed: Changed from ExtentTestManager to ExtentReportManager
        if (ExtentReportManager.getTest() != null) {

            // Using a clean blue alert block style inside Extent dashboard
            String extentHtmlBlock = "<div style='background-color: #e6f2ff; "
                    + "padding: 8px; "
                    + "border-left: 4px solid #0066cc; "
                    + "font-weight: bold; "
                    + "color: #003366;"
                    + "margin-top: 5px;"
                    + "margin-bottom: 5px;'>"
                    + "STEP: " + stepDescription + "</div>";

            ExtentReportManager.getTest().log(Status.INFO, extentHtmlBlock);
        }
    }
}