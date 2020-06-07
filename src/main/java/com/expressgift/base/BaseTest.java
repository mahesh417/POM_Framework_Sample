package com.expressgift.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.expressgift.page.utilities.GetConfig;
import com.expressgift.page.utilities.LogReporter;

public class BaseTest {
	protected WebDriver driver;
	protected Logger log;
	protected ExtentReports extentReport ; 
	protected ExtentHtmlReporter extentHTMLReport ; 
	protected ExtentTest extentTest ; 
	public static String logFileFilder ;

	public String logFileNameGenerator() {
		SimpleDateFormat currDateAndTime = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		return BaseTest.logFileFilder ="log"+currDateAndTime.format(new Date());
	}
	
	
	@BeforeClass
	protected void setupClass(ITestContext ctx) {
		String testName = ctx.getCurrentXmlTest().getName();
		log = Logger.getLogger(testName);
	}
	
	@BeforeTest
	public void startReporter()
	{	SimpleDateFormat currDateAndTime = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String fileName = System.getProperty("user.dir")+"\\test-output\\Report"+"\\"+logFileNameGenerator();
		extentHTMLReport = new ExtentHtmlReporter(fileName+"\\genReport"+currDateAndTime.format(new Date())+".html");
		new File(fileName).mkdir();
		new File(fileName+"\\ScreenShot").mkdir();
		extentHTMLReport.loadConfig(System.getProperty("user.dir")+"\\extent-config.xml");
		extentHTMLReport.setAppendExisting(true);
		extentReport = new ExtentReports();
		extentReport.setSystemInfo("Host Name","LocalHost");
		extentReport.setSystemInfo("User Name",System.getProperty("user.name"));
		extentReport.attachReporter(extentHTMLReport);
	}

	/*
	 * @author Varun
	 */
	@SuppressWarnings("deprecation")
	@Parameters({ "browser" })
	@BeforeMethod
	protected void methodSetup(String browser) throws IOException {
		log.info("--------------------------------------------------------------------------------------------------");
		log.info("*****************************************Started EXecution*****************************************");
		log.info("--------------------------------------------------------------------------------------------------");
		switch (browser) {
		case "firefox":
			System.setProperty(GetConfig.getConfigProperty("driver"), GetConfig.getConfigProperty("driverPath"));
			driver = new FirefoxDriver();
			new LogReporter(driver);
		
			LogReporter.addcomment(extentTest, "FireFox launched success");
			driver.get(GetConfig.getConfigProperty("url"));
			break;
		case "chrome":
			System.setProperty(GetConfig.getConfigProperty("driverChrome"), GetConfig.getConfigProperty("driverPathChrome"));
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			new LogReporter(driver);
			//LogReporter.addcomment(extentTest, "Chrome launched success");
			driver.get("https://rubrikinc--RBKUAT.cs93.my.salesforce.com");
			break;
		case "ie":
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability("InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION", true);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("requireWindowFocus", true);
			capabilities.setCapability("initialBrowserUrl", GetConfig.getConfigProperty("url"));
			System.setProperty("webdriver.ie.driver", GetConfig.getConfigProperty("driverPathIE"));
			driver = new InternetExplorerDriver(capabilities);
			new LogReporter(driver);
			LogReporter.addcomment(extentReport.createTest("methodSetup"), "IE launched success");
			break;
		default:
			throw new RuntimeException("Please select proper browser(FIREFOX, CHROME)");
		}
		driver.manage().window().maximize();
	}

	/*
	 * @author Varun
	 */
	@AfterMethod
	protected void methodTearDown(ITestResult result) throws IOException {
		
		if (result.getStatus()==ITestResult.FAILURE) {
			LogReporter.addFailurecomment(extentTest, result.getThrowable());
		}
		log.info("Browser closed.................");
		driver.quit();
		log.info("--------------------------------------------------------------------------------------------------");
		log.info("*****************************************Stopped EXecution*****************************************");
		log.info("--------------------------------------------------------------------------------------------------");
	}
	
	@AfterTest
	protected void testTearDown() {
		log.info("Browser closed.................");
		extentReport.flush();
		log.info("--------------------------------------------------------------------------------------------------");
		log.info("*****************************************Stopped EXecution*****************************************");
		log.info("--------------------------------------------------------------------------------------------------");
	}
}
