package com.detroitlabs.katalonmobileutil.testobject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.kms.katalon.core.testobject.TestObject;

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
	
	private static TestObject findObject(String type, String name) {
		
		String objectRepo = Device.isIOS() ? iOSRepository : androidRepository;

		type = type != null ? type + "/" : "";

		String object = objectRepo + '/' + type + name;

		return findTestObject(object);
	}
	
}
