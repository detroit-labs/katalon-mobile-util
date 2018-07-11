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

	public static boolean scrollListToElementWithText(String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				Logger.debug("Checking for specific element");
				@SuppressWarnings("unchecked")
				AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();				
				driver.findElementByXPath("//android.widget.CheckedTextView[@text='" + elementText + "']");
				isElementFound = true;
				Logger.debug("Found one!");
			} catch (WebDriverException ex) {
				Logger.debug("Didn't find any matching elements");
				scrollEntireList();
			}
		}
		return isElementFound;
	}	
	
	private static void scrollEntireList() {
		@SuppressWarnings("unchecked")
		AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
		List<MobileElement> listElement = driver.findElementsByClassName("android.widget.CheckedTextView");
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

}

