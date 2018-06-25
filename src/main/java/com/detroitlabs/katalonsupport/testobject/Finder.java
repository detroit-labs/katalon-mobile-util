package com.detroitlabs.katalonsupport.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import com.detroitlabs.katalonsupport.device.Device;
import com.kms.katalon.core.testobject.TestObject;

public class Finder {

	public Finder() {
		
	}
	
	private TestObject findObject(String type, String name) {
		Device device = new Device();
		String deviceOS = device.getDeviceOS();

		String objectRepo = deviceOS + " Test";

		type = type != null ? type + "/" : "";

		String object = objectRepo + '/' + type + name;

		return findTestObject(object);
	}

	TestObject findButton(String name) {
		return findObject("Buttons", name);
	}

	TestObject findAlert(String name) {
		return findObject("Alerts", name);
	}

	TestObject findTextField(String name) {
		return findObject("Text Fields", name);
	}

	TestObject findTab(String name) {
		return findObject("Tabs", name);
	}

	TestObject findLabel(String name) {
		return findObject("Labels", name);
	}

	TestObject findGeneric(String name) {
		return findObject(null, name);
	}
	
}
