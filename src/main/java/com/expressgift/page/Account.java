package com.expressgift.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.expressgift.base.PageObject;
import com.expressgift.page.utilities.Identifier;
import com.expressgift.page.utilities.LogReporter;

public class Account extends PageObject<LoginPage> {
	
	Identifier elementIdentierHolder = new Identifier(Account.class);
	public Account(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public boolean createAccount(String accountName) throws Exception{
		
		wait(20);
		
		longWaitForElementVisible(elementIdentierHolder.getProperty("accountTab"), "xpath");
		clickElement(elementIdentierHolder.getProperty("accountTab"), "xpath");
		
		longWaitForElementVisible(elementIdentierHolder.getProperty("account_new_btn"), "xpath");
		clickElement(elementIdentierHolder.getProperty("account_new_btn"), "xpath");
		
		
		longWaitForElementVisible(elementIdentierHolder.getProperty("account_new_page"), "xpath");

		clickElement(elementIdentierHolder.getProperty("new_rceord_type"), "xpath");

		clickElement(elementIdentierHolder.getProperty("new_record_option"),"xpath");
		
		clickElement(elementIdentierHolder.getProperty("account_continue_btn"),"xpath");

		longWaitForElementVisible(elementIdentierHolder.getProperty("account_edit_page"), "xpath");

		longWaitForElementVisible(elementIdentierHolder.getProperty("account_Name"), "xpath");
	
		enterText(elementIdentierHolder.getProperty("account_Name"), "xpath", accountName);
		
		clickElement(elementIdentierHolder.getProperty("account_save"),"xpath");
	
		longWaitForElementVisible(elementIdentierHolder.getProperty("selected_record_type"), "xpath");
		
		
		String selectedRecordtype =getText(elementIdentierHolder.getProperty("selected_record_type"),"xpath");
				
		if (selectedRecordtype.contains("Customer/Prospect")) {
	        
			return true;
		} else {
			return false;
		}	
	
	}

}
