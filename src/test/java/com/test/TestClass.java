package com.test;

import static org.testng.Assert.assertTrue;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.expressgift.base.BaseTest;
import com.expressgift.base.CSVdataProvider;
import com.expressgift.page.Account;
import com.expressgift.page.Contact;
import com.expressgift.page.LoginPage;
import com.expressgift.page.LogoutPage;
import com.expressgift.page.Opportunity;
import com.expressgift.page.utilities.LogReporter;

public class TestClass extends BaseTest {


	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CSVdataProvider.class)
	
	public void OrderManagement(Map<String, String> testData) throws Exception {
		
		ExtentTest test = extentReport.createTest("OrderManagement");
	
		LoginPage loginpage = new LoginPage(driver);
		loginpage.enterUserId(testData.get("userName"));
		loginpage.enterPassword(testData.get("password"));
		loginpage.loginSubmit();
		LogReporter.addcomment(test,"UserName and Password entered Successfully");
		
		Account account = new Account(driver);
		
		Assert.assertTrue(account.createAccount(testData.get("accountname")),"Uanble to create account");
		LogReporter.addcomment(test,"Account created Successfully");
		
		Contact contact = new Contact(driver);
		Assert.assertTrue(contact.createContact(testData.get("lastName"),testData.get("emailId")));
		LogReporter.addcomment(test,"Contact created Successfully");
		
		Opportunity opp = new Opportunity(driver);
		Assert.assertTrue(opp.createOpportunity(testData.get("accountname")));
		LogReporter.addcomment(test,"Opportunity created Successfully");
		
	}

}
