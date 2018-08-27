package com.detroitlabs.katalonmobileutil.testobject;

import com.detroitlabs.katalonmobileutil.device.Device;

public class XPathBuilder {

	// TODO: Make this more generic to build xpath for all types?
	public static String xpathForLabel(String resourceId) {
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
		return "(substring(@class, string-length(@class) - string-length('TextView') + 1) = 'TextView')";
	}	
	
	private static String labelXPath(String labelText) {
		if (Device.isIOS()) {
			return "@label = '" + labelText + "'";
		} else {
			return "@text = '" + labelText + "'";
		}
	}	
	
}
