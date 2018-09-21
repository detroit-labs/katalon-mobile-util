package com.detroitlabs.katalonmobileutil.testobject;

import java.util.List;

import com.detroitlabs.katalonmobileutil.device.Device;

public class XPathBuilder {

	public static String createXPath(String type) {
		String xpath = "";
		if (Device.isIOS()) {
			xpath = "//*[equals(@type, '" + type + "')]";
		} else {
			xpath = "//*[contains(@class, '" + type + "')]";
		}
		return xpath;
	}
	
	public static String createXPath(List<String> types) {
		String xpath = "//*[(";
		
		for (int i=0; i<types.size(); i++) {
			
			if (Device.isIOS()) {
				xpath = xpath + "equals(@type, '" + types.get(i) + "')";
			} else {
				xpath = xpath + "contains(@class, '" + types.get(i) + "')";
			}
			
			if (types.size() > i+1) {
				xpath = xpath + " or ";
			}
			
		}
		
		xpath = xpath + ")]";
		
		return xpath;
	}
	
	// TODO: Make this more generic to build xpath for all types?
	public static String xpathForLabelWithResourceId(String resourceId) {
		return xpathForLabel(resourceId, null);
	}
	
	public static String xpathForLabel(String resourceId, String labelText) {
		String xpath = "";
		
		if (Device.isIOS()) {
			xpath = "//XCUIElementTypeStaticText[" + resourceIdXPath(resourceId);
		} else {
			xpath = "//*[" + textViewXPath() + " and " + resourceIdXPath(resourceId);
		}
		
		xpath = xpath + (labelText != null ? " and " + labelXPath(labelText) : "");
		xpath = xpath + "]";

		return xpath;
	}
	
	public static String xpathForLabelWithText(String labelText) {
		String xpath = "";
		
		if (Device.isIOS()) {
			xpath = "//XCUIElementTypeStaticText[";
		} else {
			xpath = "//*[" + textViewXPath();
		}
		
		xpath = xpath + (labelText != null ? " and " + labelXPath(labelText) : "");
		xpath = xpath + "]";

		return xpath;
	}	
	
	public static String xpathForCheckboxWithText(String checkboxText) {
		String xpath = "";
		
		if (Device.isIOS()) {
			xpath = "//XCUIElementTypeStaticText[";
		} else {
			xpath = "//android.widget.CheckBox[";
		}
		
		xpath = xpath + (checkboxText != null ? labelXPath(checkboxText) : "");
		xpath = xpath + "]";

		return xpath;
	}
	
	public static String addVisible(String xpath) {
		// find end of the xpath string
		// check if there are any statements in it already (if so, add an "and")
		// add a visible statement
		// TODO: Handle an xpath that is an array
		return null;
	}
	
	public static String addLabel(String xpath, String label) {
		// find end of xpath string
		// check if there are any statements in it already (if so, add an "and")
		// add a label= statement
		// TODO: Handle an xpath that is an array
		return null;
	}
	
	private static String resourceIdXPath(String resourceId) {
		if (Device.isIOS()) {
			// The Xcode accessibility id comes through as "name" in the page document
			return "@name = '" + resourceId + "'";
		} else {
			// Using "contains" for Android because the resource-id gets appended with package info
			return "contains(@resource-id, '" + resourceId + "')";
		}
	}
	
	private static String textViewXPath() {
		// Android has multiple classes that count as text views
		return "contains(@class, 'TextView')";
	}	
	
	private static String labelXPath(String labelText) {
		if (Device.isIOS()) {
			return "@label = '" + labelText + "'";
		} else {
			return "@text = '" + labelText + "'";
		}
	}	
	
}
