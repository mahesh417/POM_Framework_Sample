package com.expressgift.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.expressgift.base.PageObject;
import com.expressgift.page.utilities.Identifier;

public class LoginPage extends PageObject<LoginPage> {
	
	Identifier elementIdentierHolder = new Identifier(LoginPage.class);
	
	
	public LoginPage(WebDriver driver) {
		
		super(driver);
		// TODO Auto-generated constructor stub
	}
	

	public void enterUserId(String value) throws Exception{
		
		waitForPage(By.xpath(elementIdentierHolder.getProperty("userId")));
		enterText(elementIdentierHolder.getProperty("userId"), "xpath", value);
	}
	
	public void enterPassword(String value) throws Exception{
		clickElement(elementIdentierHolder.getProperty("password"), "xpath");
		enterText(elementIdentierHolder.getProperty("password"), "xpath", value);
	}
	
	public void loginSubmit() throws Exception{
		clickElement(elementIdentierHolder.getProperty("submitButton"), "xpath");
	}

	
}

