package com.detroitlabs.katalonmobileutil.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;

public class Finder {

	private static String iOSRepository = "iOS Objects";
	private static String androidRepository = "Android Objects";

	public static void setIOSRepository(String repository) {
		iOSRepository = repository;
	}

	public static void setAndroidRepository(String repository) {
		androidRepository = repository;
	}

	public static TestObject findAlert(String name) {
		return findObject("Alerts", name);
	}

	public static TestObject findButton(String name) {
		return findObject("Buttons", name);
	}

	public static TestObject findCheckbox(String name) {
		return findObject("Checkboxes", name);
	}

	public static TestObject findImage(String name) {
		return findObject("Images", name);
	}

	public static TestObject findLabel(String name) {
		return findObject("Labels", name);
	}

	public static TestObject findLink(String name) {
		return findObject("Links", name);
	}

	public static TestObject findSegmentedControl(String name) {
		return findObject("Segmented Controls", name);
	}

	public static TestObject findSwitch(String name) {
		return findObject("Switches", name);
	}

	public static TestObject findTab(String name) {
		return findObject("Tabs", name);
	}

	public static TestObject findTextField(String name) {
		return findObject("Text Fields", name);
	}

	public static TestObject findGeneric(String name) {
		return findObject(null, name);
	}

	/**
	 * Find a Label at a given index from a list of similar Labels on the screen.
	 * <p>
	 * There must already exist a TestObject in the "Labels" Object Repository that
	 * will serve as a template.
	 * <p>
	 * For iOS, the TestObject should have the "type" property of
	 * "XCUIElementTypeStaticText" and the "name" property that matches the
	 * accessibility id of the collection of labels.
	 * <p>
	 * For Android, the TestObject should have a "class" property that ends in
	 * "TextView" and a "resource-id" property that contains the resource-id shared
	 * by the collection of labels.
	 * 
	 * @param testObjectName
	 *            the name of the Label in the Object Repository which will be used
	 *            as the template for finding all of the labels with the same
	 *            class/type and accessibility/resource id.
	 * @param index
	 *            the place of the element in the list that you want to return. The
	 *            first element is at index 1.
	 * @return the matching Label TestObject
	 */
	public static TestObject findLabelAtIndex(String testObjectName, int index) {

		AppiumDriver<?> driver = MobileDriverFactory.getDriver();

		TestObject templateObject = Finder.findLabel(testObjectName);
		String resourceId = TestObjectConverter.resourceIdFromTestObject(templateObject);

		String xpath = "(" + XPathBuilder.xpathForLabelWithResourceId(resourceId) + ")[" + index + "]";

		RemoteWebElement listElement = null;

		Logger.debug("Looking for labels with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);
		} catch (WebDriverException ex) {
			String resourceType = Device.isIOS() ? "accessibility id" : "resource-id";
			throw (new ListItemsNotFoundException(xpath, resourceId, resourceType, index));
		}

		Logger.debug("Found label:" + listElement);
		TestObject objectAtIndex = TestObjectConverter.fromElement(listElement);

		// return the test object
		return objectAtIndex;
	}

	/**
	 * Find a Label with given text from a list of all text views on the screen.
	 * <p>
	 * There is no prerequisite that a similar Label exists in the Object
	 * Repository.
	 * 
	 * @param labelText
	 *            the text of the label to find. Must match exactly, including
	 *            upper/lower case matching.
	 * @return the matching Label TestObject
	 */
	public static TestObject findLabelWithText(String labelText) {

		AppiumDriver<?> driver = MobileDriverFactory.getDriver();

		String xpath = XPathBuilder.xpathForLabelWithText(labelText);

		RemoteWebElement listElement = null;

		Logger.debug("Looking for labels with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);
		} catch (WebDriverException ex) {
			throw (new ListItemsNotFoundException(xpath, labelText));
		}

		Logger.debug("Found label:" + listElement);
		TestObject objectAtIndex = TestObjectConverter.fromElement(listElement);

		// return the test object
		return objectAtIndex;
	}

	/**
	 * Find a Label with given text from a list of similar Labels on the screen.
	 * <p>
	 * There must already exist a TestObject in the "Labels" Object Repository that
	 * will serve as a template.
	 * <p>
	 * For iOS, the TestObject should have the "type" property of
	 * "XCUIElementTypeStaticText" and the "name" property that matches the
	 * accessibility id of the collection of labels.
	 * <p>
	 * For Android, the TestObject should have a "class" property that ends in
	 * "TextView" and a "resource-id" property that contains the resource-id shared
	 * by the collection of labels.
	 * 
	 * @param testObjectName
	 *            the name of the Label in the Object Repository which will be used
	 *            as the template for finding all of the labels with the same
	 *            class/type and accessibility/resource id.
	 * @param labelText
	 *            the text of the label to find. Must match exactly, including
	 *            upper/lower case matching.
	 * @return the matching Label TestObject
	 */
	public static TestObject findLabelWithText(String testObjectName, String labelText) {

		AppiumDriver<?> driver = MobileDriverFactory.getDriver();

		TestObject templateObject = Finder.findLabel(testObjectName);
		String resourceId = TestObjectConverter.resourceIdFromTestObject(templateObject);

		String xpath = XPathBuilder.xpathForLabel(resourceId, labelText);

		RemoteWebElement listElement = null;

		Logger.debug("Looking for labels with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);
		} catch (WebDriverException ex) {
			String resourceType = Device.isIOS() ? "accessibility id" : "resource-id";
			throw (new ListItemsNotFoundException(xpath, resourceId, resourceType, labelText));
		}

		Logger.debug("Found label:" + listElement);
		TestObject objectAtIndex = TestObjectConverter.fromElement(listElement);

		// return the test object
		return objectAtIndex;
	}

	/**
	 * Find a Checkbox with given text from a list of all checkboxes on the screen.
	 * <p>
	 * There is no prerequisite that a similar Checkbox exists in the Object
	 * Repository.
	 * 
	 * @param checkboxText
	 *            the text of the checkbox to find. Must match exactly, including
	 *            upper/lower case matching.
	 * @return the matching Checkbox TestObject
	 */
	public static TestObject findCheckboxWithText(String checkboxText) {

		AppiumDriver<?> driver = MobileDriverFactory.getDriver();

		String xpath = XPathBuilder.xpathForCheckboxWithText(checkboxText);

		RemoteWebElement listElement = null;

		Logger.debug("Looking for checkboxes with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);
		} catch (WebDriverException ex) {
			throw (new ListItemsNotFoundException(xpath, checkboxText));
		}

		Logger.debug("Found checkbox:" + listElement);
		TestObject objectAtIndex = TestObjectConverter.fromElement(listElement);

		// return the test object
		return objectAtIndex;
	}

	private static TestObject findObject(String type, String name) {

		String objectRepo = Device.isIOS() ? iOSRepository : androidRepository;

		type = type != null ? type + "/" : "";

		String object = objectRepo + '/' + type + name;

		return findTestObject(object);
	}

}
