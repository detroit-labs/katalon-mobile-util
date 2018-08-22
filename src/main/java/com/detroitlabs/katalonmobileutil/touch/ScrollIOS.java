package com.detroitlabs.katalonmobileutil.touch;

import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class ScrollIOS {

	public static boolean scrollListToElementWithText(String accessibilityId, String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				 Logger.debug("Checking for specific element: " + elementText);
				// The Xcode accessibility id comes through as "name" in the page document
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();				 
				driver.findElementByXPath("//XCUIElementTypeStaticText[@name='" + accessibilityId
						+ "' and @visible ='true' and @label='" + elementText + "']");
				isElementFound = true;
				 Logger.debug("Found an element in the current scroll list.");
			} catch (WebDriverException ex) {
				 Logger.debug("Didn't find any matching elements.");
				scrollEntireList(accessibilityId);
			}
		}
		return isElementFound;
	}
	
	private static void scrollEntireList(String accessibilityId) {
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
		
		// The Xcode accessibility id comes through as "name" in the page document
		String xpath = "//XCUIElementTypeStaticText[@name='" + accessibilityId + "' and @visible ='true']";
		
		@SuppressWarnings("unchecked")
		List<RemoteWebElement> listElement = (List<RemoteWebElement>) driver.findElementsByXPath(xpath);
		
		Logger.debug("Getting a scroll list of all elements.");
		if (listElement.size() <= 0) {
			// throw a new exception for not being able to find the elements
			throw(new ListItemsNotFoundException(xpath, accessibilityId, "accessibility id"));
		} 
			
		RemoteWebElement bottomElement = listElement.get(listElement.size() - 1);
		RemoteWebElement topElement = listElement.get(0);	
		
		// Press and scroll from the last element in the list all the way to the top
		Logger.debug("Scrolling...");
		TouchAction touchAction = new TouchAction(driver);
		touchAction.longPress(bottomElement).moveTo(topElement).release().perform();
		
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(5);
	}	

}
