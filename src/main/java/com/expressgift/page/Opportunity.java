package com.expressgift.page;

import org.openqa.selenium.WebDriver;

import com.expressgift.base.PageObject;
import com.expressgift.page.utilities.Identifier;

public class Opportunity extends PageObject<LoginPage> {
	
	Identifier elementIdentierHolder = new Identifier(Account.class);
	public Opportunity(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean createOpportunity(String expectedAccountName) throws Exception{
		
		longWaitForElementVisible(elementIdentierHolder.getProperty("subTabOpportunity"),"xpath");
		mouseHoverJScript(elementIdentierHolder.getProperty("subTabOpportunity"), "xpath");
        clickElement(elementIdentierHolder.getProperty("newButtonOpp"), "xpath");
        //longWaitForElementVisible(elementIdentierHolder.getProperty("newOpportunityPageIdentifier"),"xpath");
		
	
        longWaitForElementVisible(elementIdentierHolder.getProperty("accountname_On_Oppertunity"),"xpath");
        String actualAccountName = getWebElement(elementIdentierHolder.getProperty("accountname_On_Oppertunity"),"xpath").getAttribute("value");
 
		if (expectedAccountName.equalsIgnoreCase(actualAccountName)) {
	
               scrollToElement(elementIdentierHolder.getProperty("page_scroll"));
               clickElement(elementIdentierHolder.getProperty("dealInitiated_type"), "xpath");
               clickElement(elementIdentierHolder.getProperty("save_opportunity"), "xpath");

               longWaitForElementVisible(elementIdentierHolder.getProperty("details_tab"),"xpath");
               clickElement(elementIdentierHolder.getProperty("details_tab"), "xpath");
               return true;

	}
		
		else {
			
		return false;
			
		}
		
		
	}
		
	

}
