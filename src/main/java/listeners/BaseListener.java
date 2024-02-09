package listeners;

import java.lang.annotation.Annotation;
import java.util.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import configs.Config;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;

import tests.BaseTest;
import reports.Report;



public class BaseListener implements ITestListener {
	public static ExtentReports extentReport;
	public static HashMap<String, ExtentTest> extentTests;

	public static String getTestName(ITestNGMethod iTestNGMethod) {
		Annotation[] annotations =  (Annotation[]) iTestNGMethod.getConstructorOrMethod().getMethod().getAnnotations();

		for (Annotation annotation : annotations) {
			if (annotation instanceof Report) {
				Report report = (Report) annotation;
				return report.name();
			}
		}

		return iTestNGMethod.getConstructorOrMethod().getMethod().getName();
	}

	public static String getTestDescription(ITestNGMethod iTestNGMethod) {
		Annotation[] annotations =  (Annotation[]) iTestNGMethod.getConstructorOrMethod().getMethod().getAnnotations();

		for (Annotation annotation : annotations) {
			if (annotation instanceof Report) {
				Report report = (Report) annotation;
				return report.description();
			}
		}

		return "";
	}

	public static ExtentTest getExtentTest(String testName, String testDescription) {
		ExtentTest extentTest = BaseListener.extentTests.get(testName);
		if(extentTest == null) {
			extentTest = BaseListener.extentReport.createTest(testName, testDescription);
			BaseListener.extentTests.put(testName, extentTest);
		}
		return extentTest;
	}

	public static ExtentTest getExtentTest(ITestNGMethod iTestNGMethod) {
		String testName = BaseListener.getTestName(iTestNGMethod);
		String testDescription = BaseListener.getTestDescription(iTestNGMethod);
		return BaseListener.getExtentTest(testName, testDescription);
	}

	public static ExtentTest getExtentTest(ITestResult iTestResult) {
		ITestNGMethod iTestNGMethod = iTestResult.getMethod();
		return BaseListener.getExtentTest(iTestNGMethod);
	}

	@Override
	public void onStart(ITestContext iTestContext) {
		if (BaseListener.extentReport == null) {
			BaseListener.extentReport = new ExtentReports();

			ExtentSparkReporter reporter = new ExtentSparkReporter(Config.reportPath + "/" + Config.reportName + ".html");
			reporter.config().setReportName("Safewalk Test Suite Report");

			BaseListener.extentReport.attachReporter(reporter);
			BaseListener.extentReport.setSystemInfo("Author", "Altipeak");
		}

		if (BaseListener.extentTests == null) {
			BaseListener.extentTests = new HashMap<>();
		}

		List<ITestNGMethod> testsMethods = iTestContext.getSuite().getAllMethods();
		testsMethods.sort(Comparator.comparing(BaseListener::getTestName));

		for (ITestNGMethod method : testsMethods) {
			BaseListener.getExtentTest(method);
		}

	}

	@Override
	public void onFinish(ITestContext iTestContext) {
		BaseListener.extentReport.flush();
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		// iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		ExtentTest extentTest = BaseListener.getExtentTest(iTestResult);
		extentTest.log(Status.PASS, "Test passed");
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		Object testClass = iTestResult.getInstance();
		WebDriver driver = ((BaseTest) testClass).getDriverInstance();

		ExtentTest extentTest = BaseListener.getExtentTest(iTestResult);

		if (driver != null) {
			try {
				String base64Screenshot = "data:image/png;base64,"
						+ ((TakesScreenshot) Objects.requireNonNull(driver)).getScreenshotAs(OutputType.BASE64);
				Media media = extentTest.addScreenCaptureFromBase64String(base64Screenshot).getModel().getMedia().get(0);
					
				Throwable cause = iTestResult.getThrowable();
					
				if(cause != null) {
					extentTest.log(Status.FAIL, ExceptionUtils.getMessage(cause), media);
				} else {
					extentTest.log(Status.FAIL, "Test Failed", media);
				}
					
				return;
			} catch(Exception ignored) {}
		}
			
		Throwable cause = iTestResult.getThrowable();
			
		if(cause != null) {
			extentTest.log(Status.FAIL, ExceptionUtils.getMessage(cause));
		} else {
			extentTest.log(Status.FAIL, "Test Failed");
		}
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		ExtentTest extentTest = BaseListener.getExtentTest(iTestResult);
		extentTest.log(Status.SKIP, "Test Skipped");
	}
}
