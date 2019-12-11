package com.detroitlabs.katalonmobileutil.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import com.detroitlabs.katalonmobileutil.device.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;

public class Finder {

	private static String iOSRepository = "iOS Objects";
	private static String androidRepository = "Android Objects";
	private static String webRepository = "Web Objects";

	private static Platform platform;

	public static void setIOSRepository(String repository) {
		iOSRepository = repository;
	}

	public static void setAndroidRepository(String repository) {
		androidRepository = repository;
	}

	public static void setWebRepository(String repository) {
		webRepository = repository;
	}

	public static void setPlatform(Platform platformPref) {
		platform = platformPref;
		Logger.debug("Finder is now using platform: " + platform.getName());
	}

	public static TestObject findAlert(String name) {
		return findObject(TestObjectType.ALERT, name);
	}

	public static TestObject findButton(String name) {
		return findObject(TestObjectType.BUTTON, name);
	}

	public static TestObject findCheckbox(String name) {
		return findObject(TestObjectType.CHECKBOX, name);
	}

	public static TestObject findContainer(String name) {
		return findObject(TestObjectType.CONTAINER, name);
	}

	public static TestObject findImage(String name) {
		return findObject(TestObjectType.IMAGE, name);
	}

	public static TestObject findLabel(String name) {
		return findObject(TestObjectType.LABEL, name);
	}

	public static TestObject findLink(String name) {
		return findObject(TestObjectType.LINK, name);
	}

	public static TestObject findSegmentedControl(String name) {
		return findObject(TestObjectType.SEGMENTED_CONTROL, name);
	}

	public static TestObject findSwitch(String name) {
		return findObject(TestObjectType.SWITCH, name);
	}

	public static TestObject findTab(String name) {
		return findObject(TestObjectType.TAB, name);
	}

	public static TestObject findTextField(String name) {
		return findObject(TestObjectType.TEXT_FIELD, name);
	}

	public static TestObject findGeneric(String name) {
		return findObject(TestObjectType.GENERIC, name);
	}

	public static TestObject find(TestObjectType type, String name) {
		return findObject(type, name);
	}

	/**
	 * Find a TestObject at a given index from a list of similar TestObjects on the screen.
	 * <p>
	 * There must already exist a TestObject in the Object Repository that
	 * will serve as a template.
	 * <p>
	 * For iOS, the TestObject should have a "name" property that matches the
	 * accessibility id of the collection of elements.
	 * <p>
	 * For Android, the TestObject should have a "class" property and a "resource-id" property that
	 * contains the resource-id shared by the collection of elements.
	 * @param type
	 * 			  the type of the TestObject you want to find.
	 * @param testObjectName
	 *            the name of the TestObject in the Object Repository which will be used
	 *            as the template for finding all of the elements with the same
	 *            class/type and accessibility/resource id.
	 * @param index
	 *            the place of the element in the list that you want to return. The
	 *            first element is at index 1.
	 * @return the matching TestObject
	 */
	public static TestObject findElementAtIndex(TestObjectType type, String testObjectName, int index) {

		AppiumDriver<?> driver = MobileDriverFactory.getDriver();

		TestObject templateObject = Finder.find(type, testObjectName);
		String resourceId = TestObjectConverter.resourceIdFromTestObject(templateObject);
		String typeFromObject = TestObjectConverter.typeFromTestObject(templateObject);

		String xpath = "(" + XPathBuilder.addResourceId(XPathBuilder.createXPath(typeFromObject), resourceId) + ")[" + index + "]";

		RemoteWebElement listElement = null;

		Logger.debug("Looking for " + type.getName() + " with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);
		} catch (WebDriverException ex) {
			String resourceType = Device.isIOS() ? "accessibility id" : "resource-id";
			throw (new ListItemsNotFoundException(xpath, resourceId, resourceType, index));
		}

		Logger.debug("Found " + type.getName() + ":" + listElement);
		TestObject objectAtIndex = TestObjectConverter.fromElement(listElement);

		// return the test object
		return objectAtIndex;
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

		return findElementAtIndex(TestObjectType.LABEL, testObjectName, index);
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
	 * @throws ListItemsNotFoundException if the label isn't found.
	 */
	public static TestObject findLabelWithText(String testObjectName, String labelText) throws ListItemsNotFoundException {

		return Finder.findLabelWithText(testObjectName, labelText, FailureHandling.STOP_ON_FAILURE);
		
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
	 * @param failureHandling
	 * 			  how the test flow should be handled in the event that the label can't be found.
	 * @return the matching Label TestObject
	 * @throws ListItemsNotFoundException if the label isn't found, unless the failureHandling is
	 *            CONTINUE_ON_FAILURE or OPTIONAL.
	 */
	public static TestObject findLabelWithText(String testObjectName, String labelText, FailureHandling failureHandling) throws ListItemsNotFoundException {

		AppiumDriver<?> driver = MobileDriverFactory.getDriver();

		TestObject templateObject = Finder.findLabel(testObjectName);
		String resourceId = TestObjectConverter.resourceIdFromTestObject(templateObject);

		String xpath = XPathBuilder.xpathForLabel(resourceId, labelText);

		RemoteWebElement listElement = null;

		Logger.debug("Looking for labels with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);
		} catch (WebDriverException ex) {
			switch (failureHandling) {
				case OPTIONAL:
				case CONTINUE_ON_FAILURE:
					return null;
				default:
					String resourceType = Device.isIOS() ? "accessibility id" : "resource-id";
					throw (new ListItemsNotFoundException(xpath, resourceId, resourceType, labelText));	
			}
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

	private static TestObject findObject(TestObjectType type, String name) {

		String typeDirectory;
		String objectRepo;

		// First, check if the tester has set the platform for Finder directly
		if (platform != null) {
			Logger.debug("Finder is using user-specified platform: " + platform.getName() + " to find the Test Object named '" + name + "'");
			switch (platform) {
				case IOS:
					objectRepo = iOSRepository;
					break;
				case ANDROID:
					objectRepo = androidRepository;
					break;
				case WEB:
					objectRepo = webRepository;
					break;
				default:
					objectRepo = "";
			}
		} else {
			// Fall back on checking the device's platform to set the test object location
			Logger.debug("Finder is using device platform: " + Device.getDeviceOS() + " to find the Test Object named " + name + "'");
			if (Device.isWeb()) {
				objectRepo = webRepository;
			}
			else {
				objectRepo = Device.isIOS() ? iOSRepository : androidRepository;
			}
		}

		typeDirectory = type != null && type.getName() != null ? type.getName() + "/" : "";

		String object = objectRepo + '/' + typeDirectory + name;

		return findTestObject(object);
	}

}
