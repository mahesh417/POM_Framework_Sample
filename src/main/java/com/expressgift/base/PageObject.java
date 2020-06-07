package com.expressgift.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.expressgift.page.utilities.GetConfig;
import com.expressgift.page.utilities.LogReporter;


/* All core method to reuse extended by page.
 * @author Varun
 */
public class PageObject<T> {
	protected static WebDriver driver;
	protected WebDriverWait wait;
	private static FluentWait<WebDriver> shortWait = null;
	private static FluentWait<WebDriver> mediumWait = null;
	private static FluentWait<WebDriver> longWait = null;
	private static int shortWaitTime = 40;
	private static int mediumWaitTime = 60;
	private static int LongWaitTime = 120;

	@SuppressWarnings("deprecation")
	protected PageObject(WebDriver driver) {
		PageObject.driver = driver;
		wait = new WebDriverWait(driver, 60);
		shortWait = new FluentWait<WebDriver>(driver).withTimeout(shortWaitTime, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
		mediumWait = new FluentWait<WebDriver>(driver).withTimeout(mediumWaitTime, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
		longWait = new FluentWait<WebDriver>(driver).withTimeout(LongWaitTime, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
	}

	/*
	 * Only used to wait for page to load
	 */
	protected void waitForPage(By locator, Integer... timeout) {
		int attempts=0;
		while(attempts < 10) {
			try {
				waitFor(ExpectedConditions.visibilityOfElementLocated(locator), (timeout.length>0?timeout[0]:null));
				break;
			}catch(Exception e) {
				
			}
			attempts++;
		}
	}
	
	protected void waitFor(ExpectedCondition<WebElement> condition, Integer timeOutInSeconds) {
		timeOutInSeconds = timeOutInSeconds!=null?timeOutInSeconds:30;
		WebDriverWait wait=new WebDriverWait(driver, timeOutInSeconds);
		wait.until(condition);
	}
	
	protected Boolean waitForElementToAppear(String identifier, String locator) {
		long end = System.currentTimeMillis() + 30000;
		WebElement element = null;
		boolean nseLogMessage = true;
		while (System.currentTimeMillis() < end) {
			try {
				element = getWebElement(identifier, locator);
				if (element == null) {
					PageObject.wait(3000);
					System.out.println("Waiting for " + element + " to appear");
				} else {
					if (element != null) {
						break;
					}
				}
			} catch (NoSuchElementException nse) {
				if (nseLogMessage) {
					PageObject.wait(3000);
					System.out.println("Waiting for " + element + " to appear");
					nseLogMessage = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
		if (element == null) {
			try {
				WebElement errorMessage = driver.findElement(By.id("errorsDiv"));
				if (errorMessage.isDisplayed()) {
					Assert.fail("Test case failed because of " + errorMessage.getText() + ", Please try executing in different build");
				}
				return false;
			} catch (NoSuchElementException ex1) {
				Assert.fail("Unable to find  expected WebElement before timeout.");
				return false;
			}
		}
		return true;
	}

	/**
	 * isElementPresent : This method waits until element is present [dont care
	 * about visible or not]
	 * 
	 * @param identifier
	 * @param locator
	 * @param waitTime
	 */

	protected void isElementPresent(String identifier, String locator, int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.presenceOfElementLocated(locateElement(identifier, locator)));
	}

	/**
	 * isElementClickable : This method waits until element is cllickable
	 * 
	 * @param identifier
	 * @param locator
	 * @param waitTime
	 */

	protected void isElementClickable(String identifier, String locator, int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(identifier, locator)));
	}

	/**
	 * isElementVisible : This method waits until element is visible
	 * 
	 * @param identifier
	 * @param locator
	 * @param waitTime
	 */

	protected void isElementVisible(String identifier, String locator, int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locateElement(identifier, locator)));
	}

	/**
	 * isElementInVisible : This method waits until element is visible
	 * 
	 * @param WebElement
	 */

	protected void isElementVisible(WebElement element, int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * waitTillVisibilityOfAllElements : This method waits until all element are
	 * visible
	 * 
	 * @param WebElement
	 */

	protected void waitTillVisibilityOfAllElements(String identifier, String locator, int waitTime) {
		List<WebElement> linkElements = driver.findElements(locateElement(identifier, locator));
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.visibilityOfAllElements(linkElements));
	}

	/**
	 * isElementInVisible : This method waits until element is visible
	 * 
	 * @param WebElement
	 */

	protected void isElementInVisible(String identifier, String locator, int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locateElement(identifier, locator)));
	}

	/**
	 * waitUntilInvisibilityOfElementWithText : waits until text is not displayed on
	 * UI
	 */
	protected void waitUntilInvisibilityOfElementWithText(String identifier, String locator, int waitTime, String text) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.invisibilityOfElementWithText(locateElement(identifier, locator), text));
	}

	/**
	 * dragAndDrop element
	 * 
	 * @param sourceElement
	 * @param destinationElement
	 */

	protected void dragAndDrop(WebElement sourceElement, WebElement destinationElement) {
		(new Actions(driver)).dragAndDrop(sourceElement, destinationElement).perform();
	}

	/**
	 * Double clicks on the element (using Actions class)
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return void
	 * 
	 */
	protected void doubleClickElement(String identifier, String locator) throws Exception {
		WebElement element = null;
		try {
			element = getWebElement(identifier, locator);
			if (element == null) {
				throw new Exception("Not found the element: " + locator);
			}
		} catch (Exception e) {
			throw new Exception("Not able to get the Element", e);
		}
		// first move to the element in the web page
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		// Now double click on the web Element
		try {
			action.doubleClick().build().perform();
		} catch (Exception e) {
			throw new Exception("Could not Double Click the element: " + locator);
		}
	}

	/**
	 * Clicks on the element
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return void
	 * 
	 */
	protected void clickElement(String identifier, String locator) throws Exception {
		WebElement element = null;
		try {
			element = getWebElement(identifier, locator);
			if (element == null) {
				throw new Exception("Not found the element: " + identifier);
			}
		} catch (Exception e) {
			throw new Exception("Not able to get the Element: " + identifier, e);
		}
		// first move to the element in the web page
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		// Now click on the web Element
		try {
			element.click();
		} catch (Exception e) {
			throw new Exception("Could not Click the element: " + identifier);
		}
	}

	/**
	 * Enters the text in the text box by replacing the existing text (clearing the
	 * old data)
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @param data
	 *            >> The value to be entered into the text box
	 * @return void
	 * 
	 */
	protected void enterText(String identifier, String locator, String data) throws Exception {
		WebElement element = null;
		try {
			element = getWebElement(identifier, locator);
			if (element == null) {
				throw new Exception("Not found the element: " + identifier);
			}
		} catch (Exception e) {
			throw new Exception("Not found the element: " + identifier);
		}
		// first move to the element in the web page
		/*Actions action = new Actions(driver);
		action.moveToElement(element).click().build().perform();*/
		try {
			// element.click();
			element.clear();
			element.sendKeys(data);
		} catch (Exception e) {
			throw new Exception("Not able to enter the text in the field: " + identifier);
		}
	}

	protected void enterTextWithJavascript(String identifier, String locator, String data) throws Exception {

		WebElement ele = getWebElement(identifier, locator);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].value='" + data + "'", ele);
	}

	/**
	 * Used to get the text value from the webelement
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param locator
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return Extracted text from the webelement
	 */
	protected String getText(String identifier, String locator) throws Exception {
		String returnText = null;
		WebElement element = null;
		try {
			element = getWebElement(identifier, locator);
			returnText = element.getText();
			System.out.println("Return text for identifier: " + element + ", is: " + returnText);
			return returnText;
		} catch (Exception e) {
			throw new Exception("Could not get the webElement to get the Text", e);
		}
	}

	/**
	 * Used to get the Webelement checkbox value is Checked or not
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return true if element is Selected, false otherwise
	 * 
	 */
	protected boolean verifyElementIsSelected(String identifier, String locator) throws Exception {
		try {
			WebElement ele = getWebElement(identifier, locator);
			boolean status = ele.isSelected();
			return status;
		} catch (Exception e) {
			throw new Exception("Could not the checkbox webElement", e);
		}
	}

	/**
	 * Used to get the Webelement display status in the current web page
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return true if element is Displayed, false otherwise
	 * 
	 */
	protected boolean verifyElementIsDisplayed(String identifier, String locator) throws Exception {
		try {
			Thread.sleep(2000);
			boolean elementPresent = false;
			WebElement ele = getWebElement(identifier, locator);
			elementPresent = ele.isDisplayed();
			System.out.println(elementPresent);
			return elementPresent;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Used to get the Webelement (with the combination of the 'locator' and
	 * 'identifier' params)
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param identifier
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return Webelement if it is located, null if not
	 * 
	 */
	protected WebElement getWebElement(String identifier, String locator) throws Exception {
		By loc = getByLocator(identifier, locator);
		WebElement element = null;
		String wait = GetConfig.getConfigProperty("Wait");
		int waitTime = 0;
		switch (wait) {
		case "short":
			waitTime = shortWaitTime;
			break;
		case "medium":
			waitTime = mediumWaitTime;
			break;
		case "long":
			waitTime = LongWaitTime;
			break;
		default:
			waitTime = 60;
			break;
		}
		for (int count = 0; count < waitTime; count++) {
			element = driver.findElement(loc);
			if (element == null) {
				Thread.sleep(1000);
			} else {
				if (isVisibleInViewport(element)) {
					return element;
				} else {
					scrollToElement(element);
					return element;
				}
			}
		}
		return element;
	}

	protected static Boolean isVisibleInViewport(WebElement element) {

		WebDriver driver = ((RemoteWebElement) element).getWrappedDriver();

		return (Boolean) ((JavascriptExecutor) driver)
				.executeScript("var elem = arguments[0],                 " + "  box = elem.getBoundingClientRect(),    " + "  cx = box.left + box.width / 2,         " + "  cy = box.top + box.height / 2,         " + "  e = document.elementFromPoint(cx, cy); "
						+ "for (; e; e = e.parentElement) {         " + "  if (e === elem)                        " + "    return true;                         " + "}                                        " + "return false;                            ", element);
	}

	protected By getByLocator(String identifier, String locator) {

		switch (locator) {
		case "linktext":
			return By.linkText(identifier);
		case "id":
			return By.id(identifier);
		case "name":
			return By.name(identifier);
		case "xpath":
			return By.xpath("" + identifier + "");
		case "cssselector":
			return By.cssSelector(identifier);
		case "partiallinktext":
			return By.partialLinkText(identifier);
		case "classname":
			return By.className(identifier);
		case "tagname":
			return By.tagName(identifier);
		}

		return null;

	}

	/**
	 * User to locate By Element
	 * 
	 * @param identifier
	 *            ,Locator
	 * @return a By which locates A elements by identifier
	 * 
	 */
	protected By locateElement(String identifier, String locator) {
		if (locator.toLowerCase().contains("linktext")) {
			return By.linkText(identifier);
		} else if (locator.toLowerCase().contains("id")) {
			return By.id(identifier);
		} else if (locator.toLowerCase().contains("name")) {
			return By.name(identifier);
		} else if (locator.toLowerCase().contains("xpath")) {
			return By.xpath("" + identifier + "");
		} else if (locator.toLowerCase().contains("cssselector")) {
			return By.cssSelector(identifier);
		} else if (locator.toLowerCase().contains("partiallinktext")) {
			return By.partialLinkText(identifier);
		} else if (locator.toLowerCase().contains("classname")) {
			return By.className(identifier);
		} else if (locator.toLowerCase().contains("tagname")) {
			return By.tagName(identifier);
		}
		return null;

	}

	/**
	 * Used to Clear the text box value
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return void
	 * 
	 */
	protected void clearData(String identifier, String locator) throws Exception {

		try {
			WebElement ele;
			ele = getWebElement(identifier, locator);
			ele.clear();
		} catch (Exception e) {
			throw new Exception("Could not clear the data.", e);
		}
	}

	/**
	 * Used to Select the Check box (check)
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return void
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected void checkChkBox(String identifier, String locator) throws Exception {
		try {
			WebElement element = getWebElement(identifier, locator);
			if (!element.isSelected()) {
				((PageObject) element).clickElement(identifier, locator);
			}
		} catch (Exception e) {
			throw new Exception("Could not check the CheckBox.", e);
		}
	}

	/**
	 * Used to Select the UnCheck box
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return void
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected void uncheckChkBox(String identifier, String locator) throws Exception {

		try {
			WebElement element = getWebElement(identifier, locator);
			if (element.isSelected()) {
				((PageObject) element).clickElement(identifier, locator);
			}
		} catch (Exception e) {
			throw new Exception("Could not Uncheck the CheckBox.", e);
		}
	}

	/**
	 * Used to Select the Combobox value by the 'Visible text' option.
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @param selectvalue
	 *            >> Value to be selected from the Combobox
	 * @return void
	 * 
	 */
	protected void selectComboBoxByVisibleText(String identifier, String locator, String selectvalue) throws Exception {
		try {
			Select obj = new Select(getWebElement(identifier, locator));
			obj.selectByVisibleText(selectvalue);
		} catch (Exception e) {
			throw new Exception("Could not select the value in the Combo box.", e);
		}
	}

	/**
	 * Used to Select the Combobox value by the 'Index' option (on the basis of
	 * appearance in the option tag).
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @param index
	 *            >> Index value to be selected from the Combobox
	 * @return void
	 * 
	 */
	protected void selectComboBoxByIndex(String identifier, String locator, int index) throws Exception {

		try {
			Select obj = new Select(getWebElement(identifier, locator));
			obj.selectByIndex(index);
		} catch (Exception e) {
			throw new Exception("Could not select the value in the Combo box.", e);
		}
	}

	/**
	 * Used to provide static wait for the specified time
	 * 
	 * @param time
	 *            >> Time value to make the execution pause
	 * @return void
	 * 
	 */
	protected static void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to Accept the Alert window (clicks on OK button in the Alert window)
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @return void
	 * 
	 */
	protected void acceptTheAlertWindow() throws Exception {
		driver.switchTo().alert().accept();
	}

	/**
	 * Designed to retrieve the Window Title from the current active window
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @return Window title value
	 * 
	 */
	protected String getWindowTitle() throws Exception {
		String windowTitle = "";
		try {
			windowTitle = driver.getTitle();
		} catch (Exception e) {
			throw new Exception("Could not get the window title ", e);
		}
		if (windowTitle.length() == 0) {
			throw new Exception("The window title is not present for the current window.");
		}
		return windowTitle;
	}

	/**
	 * Designed for select. Returns all options' texts as a String[].
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return All options' texts as a String[].
	 * 
	 */
	protected String[] getOptionsTexts(String identifier, String locator) throws Exception {
		String[] options = null;
		try {
			WebElement[] e = getOptions(identifier, locator);
			options = new String[e.length];
			for (int i = 0; i < options.length; i++)
				options[i] = e[i].getText();
		} catch (Exception e) {
			options = null;
			throw new Exception("Could not get all the Options of the Combobox", e);
		}
		return options;
	}

	/**
	 * Designed for select. Returns all options of this select as WebElement[].
	 * 
	 * @param webElemet
	 * @return All options as WebElement[]
	 * 
	 */
	protected WebElement[] getOptions(String identifier, String locator) throws Exception {
		WebElement[] options = null;
		try {
			WebElement webElemet = getWebElement(identifier, locator);
			List<WebElement> e = new Select(webElemet).getOptions();
			options = new WebElement[e.size()];
			e.toArray(options);
		} catch (Exception e) {
			options = null;
			throw new Exception("Could not get all the Options of the Combobox", e);
		}
		return options;
	}

	protected void refreshPage() throws Exception {
		driver.navigate().refresh();
	}

	/**
	 * Moves the mouse to the middle of the element and holds it there for 500
	 * milliseconds.
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param property
	 *            >> Webelement locator type
	 * @param keyValue
	 *            >> Property name to be picked
	 * @return true if successful, false otherwise
	 * 
	 */
	protected boolean hover(String identifier, String locator) throws Exception {
		try {
			wait(1000);
			scrollUp();
		} catch (Exception e) {
			throw new Exception("Could not find the webElement", e);
		}
		return hover(identifier, locator, "", "");
	}

	/**
	 * Moves the mouse to the middle of the element, or moves it to an offset from
	 * the top left corner, and holds it there for 500 milliseconds.
	 * 
	 * @param driver
	 *            >> Webdriver element
	 * @param element
	 *            >> WebElement object on which the mouse hover should be done
	 * @param xOffset
	 *            Offset from the top-left corner. A negative value means
	 *            coordinates left from the element.
	 * @param yOffset
	 *            Offset from the top-left corner. A negative value means
	 *            coordinates above the element.
	 * @return true if successful, false otherwise
	 * 
	 */
	protected boolean hover(String identifier, String locator, String xOffset, String yOffset) {
		boolean status = true;
		try {
			WebElement ele;
			ele = getWebElement(identifier, locator);
			if (xOffset.equals("") || yOffset.equals(""))
				new Actions(driver).moveToElement(ele).perform();
			else
				new Actions(driver).moveToElement(ele, Integer.parseInt(xOffset), Integer.parseInt(yOffset)).perform();

			wait(5000);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	protected boolean jScriptClickElement(String identifier, String locator) throws Exception {
		boolean ans = true;
		WebElement ele;
		ele = getWebElement(identifier, locator);
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", ele);

		} catch (Exception e) {
			ele.click();
			e.printStackTrace();
		}
		return ans;
	}

	protected boolean verifyElementIsPresent(String identifier, String locator) {
		boolean elementPresent = false;
		try {
			WebElement ele;
			ele = getWebElement(identifier, locator);
			elementPresent = ele.isDisplayed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elementPresent;
	}

	// To mouse hover on the element using java script
	protected void mouseHoverJScript(String identifier, String locator) throws Exception {
		WebElement ele;
		ele = getWebElement(identifier, locator);
		try {
			if (verifyElementIsPresent(identifier, locator)) {

				String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
				((JavascriptExecutor) driver).executeScript(mouseOverScript, ele);

			} else {
				System.out.println("Element was not visible to hover " + "\n");

			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with " + ele + "is not attached to the page document" + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + ele + " was not found in DOM" + e.getStackTrace());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occurred while hovering" + e.getStackTrace());
		}
	}

	protected void scrollUp() {
		try {
			driver.findElement(By.tagName("body")).sendKeys(Keys.HOME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void scrollDown() throws InterruptedException {
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight," + "document.body.scrollHeight,document.documentElement.clientHeight));");

	}

	protected void pageDown() {
		try {
			driver.findElement(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isSelected(String identifier, String locator) {
		boolean status = false;
		try {
			WebElement ele;
			ele = getWebElement(identifier, locator);
			status = ele.isSelected();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	protected boolean CompareData(String eAmt, String aAmt) throws Exception {
		// ElementOperation elt = new ElementOperation(CUR_APP);
		// String txt = elt.getText(driver, locator, keyValue);
		// String aAmt = txt.substring(17);

		if (aAmt.equals(eAmt)) {
			return true;

			// Reporter.log("Minimum amount matched...", true);
		} else {
			return false;
			// Reporter.log("Minimum amount not matched...", true);
		}
	}

	protected void scrollToElement(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	protected void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	protected void launch(String URL) {
		// TODO Auto-generated method stub
		driver.get(URL);
		driver.manage().window().maximize();

	}

	protected void pressEnter(String identifier, String locator) throws Exception {

		WebElement ele = getWebElement(identifier, locator);
		ele.sendKeys(Keys.RETURN);
	}

	protected List<WebElement> getWebElements(String identifier, String locator) throws Exception {
		try {

			switch (locator) {
			case "linktext":
				return driver.findElements(By.linkText(identifier));
			case "id":
				return driver.findElements(By.id(identifier));
			case "name":
				return driver.findElements(By.name(identifier));
			case "xpath":
				return driver.findElements(By.xpath("" + identifier + ""));
			case "cssselector":
				return driver.findElements(By.cssSelector(identifier));
			case "partiallinktext":
				return driver.findElements(By.partialLinkText(identifier));
			case "classname":
				return driver.findElements(By.className(identifier));
			case "tagname":
				return driver.findElements(By.tagName(identifier));
			}
		} catch (Exception e) {
			throw new Exception("Could not find the matching element: " + identifier, e);
		}
		return null;
	}

	protected String getInnerHtml(WebElement ele) {

		return ele.getAttribute("innerHTML");
	}

	protected List<WebElement> getDisplayedWebElements(String identifier, String locator) throws Exception {

		List<WebElement> allElements = getWebElements(identifier, locator);

		List<WebElement> displayedElements = new ArrayList<WebElement>();
		for (WebElement ele : allElements) {

			if (ele.isDisplayed()) {

				displayedElements.add(ele);
			}
		}

		return displayedElements;
	}

	protected int GenerateRandomNumer(int Index) {
		Random rand = new Random();
		return rand.nextInt(Index) + 1;
	}

	protected static HashMap<String, String> map = new HashMap<String, String>();

	protected HashMap<String, String> getWindowId() {
		wait(2000);
		Set<String> set = driver.getWindowHandles();
		Iterator<String> itr = set.iterator();

		map.put("parentWinID", itr.next());
		map.put("childWinID", itr.next());
		return map;
	}

	protected void switchwindow(String window) {
		driver.switchTo().window(window);
	}

	protected void closeCurrentWindow() {
		driver.close();
	}

	protected static void navigateBack() {
		System.out.println("Navigating to Home Page");
		driver.navigate().back();
		wait(2000);
	}

	protected void clickElement(WebElement ele) {
		// TODO Auto-generated method stub
		ele.click();
	}

	protected void shortWaitForElementVisible(String identifier, String locator) throws Exception {
		try {
			switch (locator) {
			case "id":
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(identifier)));
				break;
			case "className":
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.className(identifier)));
				break;
			case "name":
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(identifier)));
				break;
			case "xpath":
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(identifier)));
				break;
			case "linkText":
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(identifier)));
				break;
			case "cssSelector":
				shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(identifier)));
				break;
			default:
				break;
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	protected void shortWaitForElementClickable(String identifier, String locator) throws Exception {
		try {
			switch (locator) {
			case "id":
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id(identifier)));
				break;
			case "className":
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id(identifier)));
				break;
			case "name":
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id(identifier)));
				break;
			case "xpath":
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id(identifier)));
				break;
			case "linkText":
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id(identifier)));
				break;
			case "cssSelector":
				shortWait.until(ExpectedConditions.elementToBeClickable(By.id(identifier)));
				break;
			default:
				break;
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	protected void shortWaitForElementClickable(WebElement ele) throws Exception {
		try {
			shortWait.until(ExpectedConditions.elementToBeClickable(ele));
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	protected void mediumWaitForElementVisible(String identifier, String locator) throws Exception {
		try {
			switch (locator) {
			case "id":
				mediumWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(identifier)));
				break;
			case "className":
				mediumWait.until(ExpectedConditions.visibilityOfElementLocated(By.className(identifier)));
				break;
			case "name":
				mediumWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(identifier)));
				break;
			case "xpath":
				mediumWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(identifier)));
				break;
			case "linkText":
				mediumWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(identifier)));
				break;
			case "cssSelector":
				mediumWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(identifier)));
				break;
			default:
				break;
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	protected void longWaitForElementVisible(String identifier, String locator) throws Exception {
		try {
			switch (locator) {
			case "id":
				longWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(identifier)));
				break;
			case "className":
				longWait.until(ExpectedConditions.visibilityOfElementLocated(By.className(identifier)));
				break;
			case "name":
				longWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(identifier)));
				break;
			case "xpath":
				longWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(identifier)));
				break;
			case "linkText":
				longWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(identifier)));
				break;
			case "cssSelector":
				longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(identifier)));
				break;
			default:
				break;
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}
}
