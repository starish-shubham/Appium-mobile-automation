package org.example.core.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTestMap = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            // Set report file location
//            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
//            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/ExtentReports/Run_" + timestamp + "/ExtentReport.html");
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/ExtentReports/ExtentReport.html");
            // Configuration
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Mobile Automation Test Report");
            sparkReporter.config().setReportName("Automation Execution Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Platform", "Android");
            extent.setSystemInfo("Android Version", "10");
        }
        return extent;
    }

    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        extentTestMap.set(test);
        return test;
    }

//    public static synchronized ExtentTest getTest() {
//        return extentTestMap.get();
//    }

    public static ExtentTest getTest() {
        return extentTestMap.get();
    }

    public static void setTest(ExtentTest test) {
        extentTestMap.set(test);
    }

    public static void unload() {
        extentTestMap.remove();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}