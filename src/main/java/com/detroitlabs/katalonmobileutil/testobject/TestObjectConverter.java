package com.detroitlabs.katalonmobileutil.testobject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;

public class TestObjectConverter {

	public static TestObject fromElement(RemoteWebElement element) {
		return fromElement(element, null);
	}
	
	public static List<TestObject> fromElements(List<RemoteWebElement> elements) {
		List<TestObject> testObjects = new ArrayList<TestObject>();
		for (int i = 0; i < elements.size(); i++) {
			testObjects.add(fromElement(elements.get(i), i));
		}
		return testObjects;
	}
	
	protected static TestObject fromElement(RemoteWebElement element, Integer index) {
		TestObject testObject = new TestObject();
		String indexString = index != null ? "[" + index + "]" : "";
		testObject.addProperty("xpath", ConditionType.EQUALS, getXPathFromElement(element) + indexString);
		Logger.debug("New Test Object created:");
		Logger.printTestObject(testObject, LogLevel.DEBUG);
		return testObject;
	}

	protected static String getXPathFromElement(RemoteWebElement element) {
		String elementDescription = element.toString();
		return elementDescription.substring(elementDescription.lastIndexOf("-> xpath: ") + 10, elementDescription.lastIndexOf("]"));
	}
	
}