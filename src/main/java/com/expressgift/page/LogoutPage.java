package com.expressgift.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.expressgift.base.PageObject;
import com.expressgift.page.utilities.Identifier;

public class LogoutPage extends PageObject<LogoutPage> {
	Identifier elementIdentierHolder = new Identifier(LogoutPage.class);
	public LogoutPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	public void clickOnMenuButton() throws Exception{
		wait(20);
		longWaitForElementVisible(elementIdentierHolder.getProperty("menuButton"), "xpath");
		clickElement(elementIdentierHolder.getProperty("menuButton"), "xpath");
	}
	
	public void clickOnLogoutButton() throws Exception{
		longWaitForElementVisible(elementIdentierHolder.getProperty("logoutButton"), "xpath");
		clickElement(elementIdentierHolder.getProperty("logoutButton"), "xpath");
	}
	
}

