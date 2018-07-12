package com.detroitlabs.katalonsupport.android;

import java.util.List;

import org.openqa.selenium.WebDriverException;

import com.detroitlabs.katalonsupport.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

public class AndroidTouch {

	// Xpath 1 (used by Selenium) doesn't have ends-with, so this is a substitute
	// TODO: Use "contains()"?
	static String textViewXpathString = "(substring(@class, string-length(@class) - string-length('TextView') + 1) = 'TextView')";
	
	// Not all elements have a distinguishable resource-id, so just the text will have to do
	public static boolean scrollListToElementWithText(String elementText) {
		return scrollListToElementWithText(null, elementText);
	}
	
	// For Android, elementId equates to the resource-id
	public static boolean scrollListToElementWithText(String elementId, String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				Logger.debug("Checking for specific element");
				@SuppressWarnings("unchecked")
				AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();				
				driver.findElementByXPath("//*[" + textViewXpathString + resourceXpathString(elementId) + " and @text='" + elementText + "']");
				isElementFound = true;
				Logger.debug("Found one!");
			} catch (WebDriverException ex) {
				Logger.debug("Didn't find any matching elements");
				scrollEntireList(elementId);
			}
		}
		return isElementFound;
	}	
	
	private static void scrollEntireList(String elementId) {
		@SuppressWarnings("unchecked")
		AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
		List<MobileElement>listElement = driver.findElementsByXPath("//*[" + textViewXpathString + resourceXpathString(elementId) + "]");
		Logger.debug("Getting list of all elements");
		TouchAction touchAction = new TouchAction(driver);
		MobileElement bottomElement = listElement.get(listElement.size() - 1);
		MobileElement topElement = listElement.get(0);
		// Press and scroll from the last element in the list all the way to the top
		Logger.debug("Scrolling...");
		touchAction.longPress(bottomElement).moveTo(topElement).release().perform();
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(5);

	}
	
	// Can speed things up to narrow down the collection of elements by resource id
	private static String resourceXpathString(String elementId) {
		if (elementId == null) {
			return "";
		}
		
		return " and contains(@resource-id, '" + elementId + "') ";
	}

}

