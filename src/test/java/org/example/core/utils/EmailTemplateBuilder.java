package org.example.core.utils;

import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;

import java.util.Map;

public class EmailTemplateBuilder {
    public static String buildEmailBody(ISuite suite) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        StringBuilder failureDetails = new StringBuilder();

        // Gather metrics from all test modules in the suite
        Map<String, ISuiteResult> results = suite.getResults();
        for (ISuiteResult result : results.values()) {
            ITestContext context = result.getTestContext();
            passed += context.getPassedTests().size();
            failed += context.getFailedTests().size();
            skipped += context.getSkippedTests().size();

            // Extract failure reasons if any tests failed
            context.getFailedTests().getAllResults().forEach(testResult -> {
                failureDetails.append("<tr style='background-color: #fff1f0;'>")
                        .append("<td style='padding: 8px; border: 1px solid #ffccc7; color: #cf1322;'><b>")
                        .append(testResult.getName()).append("</b></td>")
                        .append("<td style='padding: 8px; border: 1px solid #ffccc7; color: #cf1322;'>")
                        .append(testResult.getThrowable() != null ? testResult.getThrowable().getMessage() : "Unknown Error")
                        .append("</td></tr>");
            });
        }

        // Generate clean HTML email layout
        return "<html><body style='font-family: Arial, sans-serif; color: #333; margin: 20px;'>"
                + "<h2>Automation Execution Summary</h2>"
                + "<p>Hi Team, </p><p>Please find below the mobile automation test execution breakdown:</p>"

                // Summary Table
                + "<table style='border-collapse: collapse; width: 50%; font-size: 14px; text-align: left; margin-bottom: 25px;'>"
                + "  <tr style='background-color: #f2f2f2;'>"
                + "    <th style='padding: 10px; border: 1px solid #ddd;'>Metric</th>"
                + "    <th style='padding: 10px; border: 1px solid #ddd;'>Count</th>"
                + "  </tr>"
                + "  <tr>"
                + "    <td style='padding: 10px; border: 1px solid #ddd; color: #2eb82e; font-weight: bold;'>Passed</td>"
                + "    <td style='padding: 10px; border: 1px solid #ddd; font-weight: bold;'>" + passed + "</td>"
                + "  </tr>"
                + "  <tr>"
                + "    <td style='padding: 10px; border: 1px solid #ddd; color: #ff3333; font-weight: bold;'>Failed</td>"
                + "    <td style='padding: 10px; border: 1px solid #ddd; font-weight: bold;'>" + failed + "</td>"
                + "  </tr>"
                + "  <tr>"
                + "    <td style='padding: 10px; border: 1px solid #ddd; color: #ffcc00; font-weight: bold;'>Skipped</td>"
                + "    <td style='padding: 10px; border: 1px solid #ddd; font-weight: bold;'>" + skipped + "</td>"
                + "  </tr>"
                + "</table>"

                // Failure Details Breakdown (Only displays if failures > 0)
                + (failed > 0 ?
                "<h3>Failure Breakdown Details</h3>"
                        + "<table style='border-collapse: collapse; width: 100%; font-size: 13px; text-align: left;'>"
                        + "  <tr style='background-color: #ff4d4f; color: white;'>"
                        + "    <th style='padding: 10px; width: 30%;'>Test Case Name</th>"
                        + "    <th style='padding: 10px; width: 70%;'>Failure Reason Summary</th>"
                        + "  </tr>" + failureDetails.toString() + "</table>" : "")

                + "<br><p><i>Note: For step-by-step visual logs and runtime screenshots, please review the attached ExtentReport.html dashboard.</i></p>"
                + "<br><p>Regards,<br><b>QA Automation Engine</b></p>"
                + "</body></html>";
    }
}
