package com.detroitlabs.katalonmobileutil.testobject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectProperty;

public class TestObjectConverter {

	/**
	 * Convert a single Selenium/Appium RemoteWebElement to a Katalon TestObject
	 * @param element RemoteWebElement to be converted
	 * @return Katalon TestObject
	 */
	public static TestObject fromElement(RemoteWebElement element) {
		return fromElement(element, null);
	}
	
	/**
	 * Convert a list of Selenium/Appium RemoteWebElements to a list of Katalon TestObjects
	 * @param elements List of RemoteWebElements to be converted
	 * @return List of Katalon TestObjects
	 */
	public static List<TestObject> fromElements(List<RemoteWebElement> elements) {
		List<TestObject> testObjects = new ArrayList<TestObject>();
		for (int i = 0; i < elements.size(); i++) {
			testObjects.add(fromElement(elements.get(i), i));
		}
		return testObjects;
	}
	
	/**
	 * Convert a Selenium/Appium RemoteWebElement (which could end up being a collection of matching elements) to a Katalon TestObject
	 * @param element RemoteWebElement to be converted
	 * @param index Array index of the particular RemoteWebElement to convert to a TestObject. In the case where there will only be one
	 *              RemoteWebElement found, pass null.
	 * @return Katalon TestObject
	 */
	public static TestObject fromElement(RemoteWebElement element, Integer index) {
		TestObject testObject = new TestObject();
		String indexString = index != null ? "[" + index + "]" : "";
		testObject.addProperty("xpath", ConditionType.EQUALS, getXPathFromElement(element) + indexString);
		Logger.debug("New Test Object created:");
		Logger.printTestObject(testObject, LogLevel.DEBUG);
		return testObject;
	}

	/**
	 * Finds the resource id from a given TestObject. 
	 * Can be used with a generic TestObject to avoid hard coding resource ids in tests.
	 * @param testObject source TestObject with the accessibility or resource id
	 * @return accessibility id (iOS) or resource id (Android) of the TestObject
	 */
	public static String resourceIdFromTestObject(TestObject testObject) {
		String resourceId = null;

		// For iOS, accessiblityId will be in the property "name", for Android it's "resource-id"
		String propertyName = Device.isIOS() ? "name" : "resource-id";

		for (TestObjectProperty p : testObject.getProperties()) {
			if (propertyName.equals(p.getName())) {
				resourceId = p.getValue();
				break;
			}
		}

		return resourceId;
	}
	
	/**
	 * Finds the type or class from a given TestObject.
	 * Can be used with a generic TestObject to avoid hard coding classes/types in tests.
	 * @param testObject source TestObject with the class or type
	 * @return type (iOS) or class (Android) of the TestObject
	 */
	public static String typeFromTestObject(TestObject testObject) {
		String type = null;

		// For iOS, type will be in the property "type", for Android it's "class"
		String propertyName = Device.isIOS() ? "type" : "class";

		for (TestObjectProperty p : testObject.getProperties()) {
			if (propertyName.equals(p.getName())) {
				type = p.getValue();
				break;
			}
		}

		return type;
	}	
	
	
	protected static String getXPathFromElement(RemoteWebElement element) {
		String elementDescription = element.toString();
		return elementDescription.substring(elementDescription.lastIndexOf("-> xpath: ") + 10, elementDescription.lastIndexOf("]"));
	}
	
}