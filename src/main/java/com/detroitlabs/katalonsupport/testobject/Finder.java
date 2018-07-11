package com.detroitlabs.katalonsupport.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import com.detroitlabs.katalonsupport.device.Device;
import com.kms.katalon.core.testobject.TestObject;

public class Finder {
	
	private static TestObject findObject(String type, String name) {
		String deviceOS = Device.getDeviceOS();

		String objectRepo = deviceOS + " Test";

		type = type != null ? type + "/" : "";

		String object = objectRepo + '/' + type + name;

		return findTestObject(object);
	}

	public static TestObject findButton(String name) {
		return findObject("Buttons", name);
	}

	public static TestObject findAlert(String name) {
		return findObject("Alerts", name);
	}

	public static TestObject findTextField(String name) {
		return findObject("Text Fields", name);
	}

	public static TestObject findTab(String name) {
		return findObject("Tabs", name);
	}

	public static TestObject findLabel(String name) {
		return findObject("Labels", name);
	}

	public static TestObject findGeneric(String name) {
		return findObject(null, name);
	}
	
}
