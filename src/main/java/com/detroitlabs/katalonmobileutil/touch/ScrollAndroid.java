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

public class ScrollAndroid {

	// Xpath 1 (used by Selenium) doesn't have ends-with, so this is a substitute
	// TODO: Use "contains()"?
	static String textViewXpathString = "(substring(@class, string-length(@class) - string-length('TextView') + 1) = 'TextView')";

	// Not all elements have a distinguishable resource-id, so just the text will
	// have to do
	public static boolean scrollListToElementWithText(String elementText) {
		return scrollListToElementWithText(null, elementText);
	}

	// For Android, elementId equates to the resource-id
	public static boolean scrollListToElementWithText(String resourceId, String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				Logger.debug("Checking for specific element: " + elementText);
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();
				driver.findElementByXPath("//*[" + textViewXpathString + resourceXpathString(resourceId)
						+ " and @text='" + elementText + "']");
				isElementFound = true;
				Logger.debug("Found an element in the current scroll list.");
			} catch (WebDriverException ex) {
				Logger.debug("Didn't find any matching elements.");
				scrollEntireList(resourceId);
			}
		}
		return isElementFound;
	}

	private static void scrollEntireList(String resourceId) {
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
		
		String xpath = "//*[" + textViewXpathString + resourceXpathString(resourceId) + "]";
		
		@SuppressWarnings("unchecked")
		List<RemoteWebElement> listElement = (List<RemoteWebElement>) driver.findElementsByXPath(xpath);
		
		Logger.debug("Getting a scroll list of all elements.");
		if (listElement.size() <= 0) {
			// throw a new exception for not being able to find the elements
			throw(new ListItemsNotFoundException(xpath, resourceId, "resource id"));
		} 
		
		RemoteWebElement bottomElement = listElement.get(listElement.size() - 1);
		// TODO: Check if the last element is the same as the previous time we scrolled, if so it means we hit the end of the list without finding the element
		// and should throw an error.
		RemoteWebElement topElement = listElement.get(0);
		
		// Press and scroll from the last element in the list all the way to the top
		Logger.debug("Scrolling...");
		TouchAction touchAction = new TouchAction(driver);
		touchAction.longPress(bottomElement).moveTo(topElement).release().perform();
		
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(5);

	}

	// Can speed things up to narrow down the collection of elements by resource id
	private static String resourceXpathString(String resourceId) {
		if (resourceId == null) {
			return "";
		}

		return " and contains(@resource-id, '" + resourceId + "') ";
	}

}
