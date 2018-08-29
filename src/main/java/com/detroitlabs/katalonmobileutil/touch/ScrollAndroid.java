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
	static String textViewXpathString = "contains(@class, 'TextView')";
	
	static RemoteWebElement lastScrolledElement = null;

	// Not all elements have a distinguishable resource-id, so just the text will
	// have to do
	public static boolean scrollListToElementWithText(String elementText) {
		return scrollListToElementWithText(null, elementText);
	}

	// For Android, elementId equates to the resource-id
	public static boolean scrollListToElementWithText(String resourceId, String elementText) {
		boolean isElementFound = false;
		boolean hasScrolledToEndOfList = false;
		while (isElementFound == false && hasScrolledToEndOfList == false) {
			try {
				Logger.debug("Checking for specific element: " + elementText);
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();
				driver.findElementByXPath("//*[" + textViewXpathString + resourceXpathString(resourceId)
						+ " and @text='" + elementText + "']");
				isElementFound = true;
				Logger.debug("Found an element in the current scroll list.");
				// reset the last scrolled element for the next time we do scrolling
				lastScrolledElement = null;
			} catch (WebDriverException ex) {
				// In this case, we're using the WebDriverException to trigger the scroll event, so it's ok that it occurs.
				Logger.debug("Didn't find any matching elements in the visible scroll list.");
				scrollEntireList(resourceId, elementText);
			}
		}
		return isElementFound;
	}

	private static void scrollEntireList(String resourceId, String elementText) {
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
		// Check if the last element is the same as the previous time we scrolled, if so,
		// it means we hit the end of the list without finding the element
		// and should throw an error.
		if (lastScrolledElement != null && lastScrolledElement.getText().equals(bottomElement.getText())) {
			Logger.error("Scrolled to the bottom of the list and we didn't find the element.");
			// reset the last scrolled element for the next time we do scrolling
			lastScrolledElement = null;
			throw(new ListItemsNotFoundException(xpath, resourceId, "resource id", elementText));
		}
		lastScrolledElement = bottomElement;
		Logger.debug("New last element: " + lastScrolledElement.getText());
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
