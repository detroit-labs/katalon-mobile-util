package com.detroitlabs.katalonmobileutil.touch;

import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.detroitlabs.katalonmobileutil.testobject.XPathBuilder;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class ScrollAndroid {
	
	static String lastScrolledElement = null;

	// Not all elements have a distinguishable resource-id, so just the text will
	// have to do.
	// WARNING: In Android, buttons are also TextViews, so if a resource-id is not specified, then
	// a footer button might be considered part of the scroll list, which could prevent scrolling
	// of the actual list elements. It is always best practice to include a resource-id if possible.
	public static boolean scrollListToElementWithText(String elementText, Integer timeout) {
		return scrollListToElementWithText(null, elementText, timeout);
	}
	
	// For Android, elementId equates to the resource-id
	public static boolean scrollListToElementWithText(String resourceId, String elementText, Integer timeout) {
		String xpath = XPathBuilder.createXPath("TextView");
		if (resourceId != null) {
			xpath = XPathBuilder.addResourceId(xpath, resourceId);
		}
		return scrollListToElementWithXPath(xpath, elementText, timeout);
	}	

	public static boolean scrollListToCheckboxWithText(String elementText, Integer timeout) {
		String xpath = XPathBuilder.createXPath("CheckBox");
		return scrollListToElementWithXPath(xpath, elementText, timeout);
	}
	
	public static boolean scrollListToElementWithXPath(String xpath, String elementText, Integer timeout) {
		boolean isElementFound = false;
		boolean hasScrolledToEndOfList = false;
		while (isElementFound == false && hasScrolledToEndOfList == false) {
			try {
				Logger.debug("Checking for specific element: " + elementText);
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();
				String xpathWithText = XPathBuilder.addLabel(xpath, elementText);
				driver.findElementByXPath(xpathWithText);
				isElementFound = true;
				Logger.debug("Found an element in the current scroll list.");
				// reset the last scrolled element for the next time we do scrolling
				lastScrolledElement = null;
			} catch (WebDriverException ex) {
				// In this case, we're using the WebDriverException to trigger the scroll event, so it's ok that it occurs.
				Logger.debug("Didn't find any matching elements in the visible scroll list.");
				// Try scrolling the list with the xpath WITHOUT the text
				scrollEntireListWithXPath(xpath, elementText, timeout);
			}
		}
		return isElementFound;
	}
	
	private static void scrollEntireListWithXPath(String xpath, String elementText, Integer timeout) {
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
				
		Logger.debug("Getting a scroll list of all elements.");
		
		@SuppressWarnings("unchecked")
		List<RemoteWebElement> listElements = (List<RemoteWebElement>) driver.findElementsByXPath(xpath);
		
		if (listElements.size() <= 0) {
			// throw a new exception for not being able to find the elements
			throw(new ListItemsNotFoundException(xpath));
		} 
		
		Logger.debug("Current list:");
		Logger.printList(listElements, LogLevel.DEBUG);
		
		RemoteWebElement bottomElement = listElements.get(listElements.size() - 1);
		Logger.debug("Bottom element is now: " + bottomElement.hashCode() + " " + bottomElement.getText());
		
		// It's possible that the very last element may be cut off and cannot be tapped.
		// In this case, the last visible list item should be considered the bottom.
		RemoteWebElement justAboveBottomElement = listElements.get(listElements.size() - 2);
		if (bottomElement.getSize().height < justAboveBottomElement.getSize().height) {
			bottomElement = justAboveBottomElement;
		}
		
		// Check if the last element is the same as the previous time we scrolled, if so,
		// it means we hit the end of the list without finding the element
		// and should throw an error.
		Logger.debug("Comparing the previous last element in the list: " + lastScrolledElement);
		Logger.debug("with the new last element in the list: " + (bottomElement != null ? bottomElement.getText() : "null"));		
		if (lastScrolledElement != null && lastScrolledElement.equals(bottomElement.getText())) {
			Logger.error("Scrolled to the bottom of the list and we didn't find the element.");
			// reset the last scrolled element for the next time we do scrolling
			Logger.debug("Resetting lastScrolledElement to null");
			lastScrolledElement = null;
			throw(new ListItemsNotFoundException(xpath, elementText));
		}
		Logger.debug("Resetting lastScrolledElement to " + bottomElement.hashCode() + " " + bottomElement.getText());
		lastScrolledElement = bottomElement.getText();
		RemoteWebElement topElement = listElements.get(0);
		
		// Press and scroll from the last element in the list all the way to the top
		Logger.debug("Scrolling...");
		TouchAction touchAction = new TouchAction(driver);
		touchAction.longPress(bottomElement).moveTo(topElement).release().perform();
		
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(timeout);
		
		Logger.debug("********** Scroll attempt complete *********** ");

	}

}
