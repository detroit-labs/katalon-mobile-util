package com.detroitlabs.katalonsupport.ios;

import java.util.List;

import org.openqa.selenium.WebDriverException;

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

public class IOSTouch {

	AppiumDriver<MobileElement> driver;
	// Logger logger;

	@SuppressWarnings("unchecked")
	IOSTouch() {
		this.driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
		// this.logger = new Logger()
	}

	private void scrollEntireList(String accessibilityId) {
		// The Xcode accessibility id comes through as "name" in the page document
		List<MobileElement> listElement = driver.findElementsByXPath(
				"//XCUIElementTypeStaticText[@name='" + accessibilityId + "' and @visible ='true']");
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

	public boolean scrollListToElementWithText(String accessibilityId, String elementText) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				// logger.log("Checking for specific element", LogLevel.DEBUG)
				// The Xcode accessibility id comes through as "name" in the page document
				driver.findElementByXPath("//XCUIElementTypeStaticText[@name='" + accessibilityId
						+ "' and @visible ='true' and @label='" + elementText + "']");
				isElementFound = true;
				// logger.log("Found one!", LogLevel.DEBUG)
			} catch (WebDriverException ex) {
				// logger.log("Didn't find any matching elements", LogLevel.DEBUG)
				scrollEntireList(accessibilityId);
			}
		}
		return isElementFound;
	}

}
