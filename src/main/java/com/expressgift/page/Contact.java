package com.expressgift.page;

import org.openqa.selenium.WebDriver;

import com.expressgift.base.PageObject;
import com.expressgift.page.utilities.Identifier;

public class Contact extends PageObject<LoginPage> {

	Identifier elementIdentierHolder = new Identifier(Account.class);

	public Contact(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public boolean createContact(String lastName,String emailId) throws Exception{
			
			
			mouseHoverJScript(elementIdentierHolder.getProperty("subTabContact"), "xpath");
            clickElement(elementIdentierHolder.getProperty("newButtonContact"), "xpath");
            longWaitForElementVisible(elementIdentierHolder.getProperty("newContactPageIdentifier"),"xpath");
            
            longWaitForElementVisible(elementIdentierHolder.getProperty("lastNameField"),"xpath");
            enterText(elementIdentierHolder.getProperty("lastNameField"), "xpath", lastName);
            
            
            longWaitForElementVisible(elementIdentierHolder.getProperty("emailField"), "xpath");
            enterText(elementIdentierHolder.getProperty("emailField"), "xpath", emailId);

	
            clickElement(elementIdentierHolder.getProperty("leadSource"), "xpath");
            
            clickElement(elementIdentierHolder.getProperty("saveContact"), "xpath");

            longWaitForElementVisible(elementIdentierHolder.getProperty("lastNameAfterSave"), "xpath");

            
           String lastNameAftersave= getText(elementIdentierHolder.getProperty("lastNameAfterSave"), "xpath");
           String emailIdAfterSave=  getText(elementIdentierHolder.getProperty("emailAfterSave"), "xpath");
           String leadSourceAfterSave= getText(elementIdentierHolder.getProperty("leadSourceAfterSave"), "xpath");
            
			// verification part

			if (lastNameAftersave.equalsIgnoreCase(lastName) && emailIdAfterSave.equalsIgnoreCase(emailId)) {

			    return true;
			}
			else {
				return false;
}
}}