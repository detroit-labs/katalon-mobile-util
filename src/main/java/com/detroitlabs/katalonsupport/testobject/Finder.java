package com.detroitlabs.katalonsupport.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import java.util.Map;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.testobject.TestObject;

public class Finder {

	public Finder() {
		
	}
	
	private TestObject findObject(String type, String name) {
		ExecutionProperties props = new ExecutionProperties(RunConfiguration.getExecutionProperties());
		String deviceOS = props.getDeviceOS();

		System.out.println("deviceOS: " + deviceOS);

		String objectRepo = deviceOS + " Test";

		type = type != null ? type + "/" : "";

		String object = objectRepo + '/' + type + name;

		System.out.println("Looking for object: " + object);

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

	private class ExecutionProperties {
		
		Map<String, Object> properties;
		
		ExecutionProperties(Map<String, Object> properties) {
			this.properties = properties;
		}
		
		@SuppressWarnings("unchecked")
		String getDeviceOS() {
			Map<String, Object> drivers = (Map<String, Object>)(this.properties.get("drivers"));
			Map<String, Object> system = (Map<String, Object>)(drivers.get("system"));
			Map<String, String> mobile = (Map<String, String>)(system.get("Mobile"));
			String deviceOS = mobile.get("deviceOS");
			return deviceOS;
		}
	}
	
	
}
