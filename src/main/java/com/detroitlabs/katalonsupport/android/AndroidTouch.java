package com.detroitlabs.katalonsupport.android;

import java.util.List;

import org.openqa.selenium.WebDriverException;

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

public class AndroidTouch {

	AppiumDriver<MobileElement> driver;
	// Logger logger;

	@SuppressWarnings("unchecked")
	AndroidTouch() {
		this.driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
		// this.logger = new Logger()
	}

	private void scrollEntireList() {
		List<MobileElement> listElement = driver.findElementsByClassName("android.widget.CheckedTextView");
		// logger.log("Getting list of all elements")
		TouchAction touchAction = new TouchAction(driver);
		MobileElement bottomElement = listElement.get(listElement.size() - 1);
		MobileElement topElement = listElement.get(0);
		// Press and scroll from the last element in the list all the way to the top
		// logger.log("Scrolling...", LogLevel.DEBUG);
		touchAction.longPress(bottomElement).moveTo(topElement).release().perform();
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(5);

	}

	public boolean scrollListToElementWithText(String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				// logger.log("Checking for specific element", LogLevel.DEBUG)
				driver.findElementByXPath("//android.widget.CheckedTextView[@text='" + elementText + "']");
				isElementFound = true;
				// logger.log("Found one!", LogLevel.DEBUG)
			} catch (WebDriverException ex) {
				// logger.log("Didn't find any matching elements", LogLevel.DEBUG)
				scrollEntireList();
			}
		}
		return isElementFound;
	}

}

