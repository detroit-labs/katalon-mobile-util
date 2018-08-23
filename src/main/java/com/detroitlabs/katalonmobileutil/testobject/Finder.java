package com.detroitlabs.katalonmobileutil.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectProperty;

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
	
	public static TestObject findLabelAtIndex(String testObjectName, int index) {
		
		// TODO: should we match the name of the test object or the accessibility id? maybe 2 separate methods?
				
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
		
		TestObject templateObject = Finder.findLabel(testObjectName);
		String resourceId = resourceIdFromTestObject(templateObject);
		
		String xpath = "";
		
		if (Device.isIOS()) {
			// The Xcode accessibility id comes through as "name" in the page document
			xpath = "(//XCUIElementTypeStaticText[@name='" + resourceId + "'])";
		} else {
			// Android has multiple classes that count as text views and the "accessibility_id" is resource-id
			String textViewXpath = "(substring(@class, string-length(@class) - string-length('TextView') + 1) = 'TextView')";
			// Using "contains" for Android because the resource-id gets appended with package info
			String resourceIdXpath = "contains(@resource-id, '" + resourceId + "')";
			xpath = "(//*[" + textViewXpath + " and " + resourceIdXpath + "])";
			
		}
		
		xpath = xpath + "[" + index + "]";
		
		RemoteWebElement listElement = null;
		
		Logger.debug("Looking for elements with xpath: " + xpath);
		try {
			listElement = (RemoteWebElement) driver.findElementByXPath(xpath);			
		} catch (WebDriverException ex) {
			throw(new ListItemsNotFoundException(xpath, resourceId, "accessibility id", index));
		}

		Logger.debug("Found element:" + listElement);
		TestObject objectAtIndex = TestObjectConverter.fromElement(listElement);
		
		// return the test object
		return objectAtIndex;
	}
	
	private static String resourceIdFromTestObject(TestObject testObject) {
		String resourceId = null;
		
		// for iOS, accessiblityId will be in the property "name", for Android it's "resource-id"
		String propertyName = Device.isIOS() ? "name" : "resource-id";
		
		for (TestObjectProperty p : testObject.getActiveProperties()) {
			if (propertyName.equals(p.getName())) {
				resourceId = p.getValue();
				break;
			}
		}
		
		return resourceId;
	}
	
	private static TestObject findObject(String type, String name) {
		
		String objectRepo = Device.isIOS() ? iOSRepository : androidRepository;

		type = type != null ? type + "/" : "";

		String object = objectRepo + '/' + type + name;

		return findTestObject(object);
	}
	
}
