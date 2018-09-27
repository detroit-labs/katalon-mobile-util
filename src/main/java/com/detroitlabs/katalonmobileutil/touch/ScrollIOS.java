package com.detroitlabs.katalonmobileutil.touch;

import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class ScrollIOS {

	static String lastScrolledElement = null;
	
	public static boolean scrollListToElementWithText(String accessibilityId, String elementText) {
		String xpath = "//XCUIElementTypeStaticText[@name='" + accessibilityId
				+ "' and @visible ='true' and @label='" + elementText + "']";
		return scrollListToElementWithXPathAndText(xpath, elementText);
	}
	
	private static boolean scrollListToElementWithXPathAndText(String xpath, String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				 Logger.debug("Checking for specific element: " + elementText);
				// The Xcode accessibility id comes through as "name" in the page document
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();				 
				driver.findElementByXPath(xpath);
				isElementFound = true;
				Logger.debug("Found an element in the current scroll list.");
				// reset the last scrolled element for the next time we do scrolling
				lastScrolledElement = null;
			} catch (WebDriverException ex) {
				// In this case, we're using the WebDriverException to trigger the scroll event, so it's ok that it occurs.
				Logger.debug("Didn't find any matching elements.");
				scrollEntireList(xpath, elementText);
			}
		}
		return isElementFound;
	}
	
	private static void scrollEntireList(String xpath, String elementText) {
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
		
		// The Xcode accessibility id comes through as "name" in the page document
//		String xpath = "//XCUIElementTypeStaticText[@name='" + accessibilityId + "' and @visible ='true']";
		
		@SuppressWarnings("unchecked")
		List<RemoteWebElement> listElement = (List<RemoteWebElement>) driver.findElementsByXPath(xpath);
		
		Logger.debug("Getting a scroll list of all elements.");
		if (listElement.size() <= 0) {
			// throw a new exception for not being able to find the elements
			throw(new ListItemsNotFoundException(xpath, null, "accessibility id"));
		} 
			
		// TODO: Handle offset when looking at header labels with a lot of subitems in between
		RemoteWebElement bottomElement = listElement.get(listElement.size() - 1);
		
		// Check if the last element is the same as the previous time we scrolled, if so,
		// it means we hit the end of the list without finding the element
		// and should throw an error.
		Logger.debug("Comparing the previous last element in the list: " + lastScrolledElement);
		Logger.debug("with the new last element in the list: " + bottomElement + " with text: " + (bottomElement != null ? bottomElement.getText() : "null"));	
		if (lastScrolledElement != null && lastScrolledElement.equals(bottomElement.getText())) {
			Logger.error("Scrolled to the bottom of the list and we didn't find the element.");
			// reset the last scrolled element for the next time we do scrolling
			lastScrolledElement = null;
			throw(new ListItemsNotFoundException(xpath, null, "accessibility id", elementText));
		}
		
		RemoteWebElement topElement = listElement.get(0);	
		
		// Press and scroll from the last element in the list all the way to the top
		Logger.debug("Scrolling...");
		TouchAction touchAction = new TouchAction(driver);
		touchAction.longPress(bottomElement).moveTo(topElement).release().perform();
		
		// Update the last scrolled element variable so we can compare it to later
		lastScrolledElement = bottomElement.getText();
		
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(5);
	}	

}
