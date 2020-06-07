package com.expressgift.page.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.expressgift.base.BaseTest;
import com.google.common.io.Files;

public class LogReporter {
	static WebDriver driver = null;
	static ExtentTest test;

	public LogReporter(WebDriver driver) {
		LogReporter.driver = driver;
	}

	public static void addcomment(ExtentTest test, String comment) throws IOException {
		LogReporter.test=test;
		test.pass(comment);
		test.pass("details", MediaEntityBuilder.createScreenCaptureFromPath(takeSceenShot()).build());
	}
	public static void addFailurecomment(ExtentTest test, Throwable throwable) throws IOException {
		LogReporter.test=test;
		test.fail(throwable);
		test.fail("details", MediaEntityBuilder.createScreenCaptureFromPath(takeSceenShot()).build());
	}

	private static String takeSceenShot() throws IOException {
		SimpleDateFormat currDateAndTime = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String imageName = "screenShot_" + currDateAndTime.format(new Date()) + ".png";
		String destination = System.getProperty("user.dir") + "\\test-output\\Report\\"+BaseTest.logFileFilder + "\\ScreenShot\\" + imageName;
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File(destination);
		Files.copy(scrFile, destFile);
		return destination;
	}
}
