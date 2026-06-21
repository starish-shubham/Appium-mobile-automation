package org.example.core.utils;

import com.aventstack.extentreports.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    // Initialize SLF4J Logger for console outputs
    private static final Logger log = LoggerFactory.getLogger(Log.class);

    /**
     * Replaces log.info - Logs to console AND Extent Report as a regular info row
     */
    public static void info(String message) {
        // 1. Print to console via SLF4J
        log.info(message);

        // 2. Print cleanly to Extent Report if a test session is active
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.INFO, message);
        }
    }

    /**
     * Replaces log.error - Logs errors to console AND marks Extent Report as failed
     */
    public static void error(String message) {
        log.error(message);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.FAIL, "<b style='color:red;'>ERROR: " + message + "</b>");
        }
    }

    /**
     * Replaces log.warn - Logs warnings to console and Extent Report
     */
    public static void warn(String message) {
        log.warn(message);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.WARNING, "<span style='color:orange;'>WARN: " + message + "</span>");
        }
    }
}
