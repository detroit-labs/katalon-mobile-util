package com.detroitlabs.katalonmobileutil.testobject;

import java.util.List;

import com.detroitlabs.katalonmobileutil.device.Device;

public class XPathBuilder {

	private static final String upperCase = "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'";
	private static final String lowerCase = "'abcdefghijklmnopqrstuvwxyz'";
	
	// TODO: Make this more generic to build xpath for all types?
	public static String xpathForLabelWithResourceId(String resourceId) {
		return xpathForLabel(resourceId, null);
	}
	
	public static String xpathForLabel(String resourceId, String labelText) {
		
		String elementType = Device.isIOS() ? "XCUIElementTypeStaticText" : "TextView";
		String xpath = createXPath(elementType);
		xpath = addResourceId(xpath, resourceId);
		if (labelText != null) {
			xpath = addLabel(xpath, labelText);
		}
		return xpath;
	}
	
	public static String xpathForLabelWithText(String labelText) {
		
		String elementType = Device.isIOS() ? "XCUIElementTypeStaticText" : "TextView";
		String xpath = createXPath(elementType);
		xpath = addLabel(xpath, labelText);
		return xpath;
	}	
	
	public static String xpathForCheckboxWithText(String checkboxText) {
		// iOS checkboxes can be activated from their labels
		String elementType = Device.isIOS() ? "XCUIElementTypeStaticText" : "CheckBox";
		String xpath = createXPath(elementType);
		xpath = addLabel(xpath, checkboxText);
		return xpath;
	}

	public static String createXPath(String type) {
		String xpath = "";
		if (Device.isIOS()) {
			xpath = "//*[@type='" + type + "']";
		} else {
			xpath = "//*[contains(@class, '" + type + "')]";
		}
		return xpath;
	}
	
	public static String createXPath(List<String> types) {
		String xpath = "//*[(";
		
		for (int i=0; i<types.size(); i++) {
			
			if (Device.isIOS()) {
				xpath = xpath + "@type='" + types.get(i) + "'";
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
	
	public static String addVisible(String xpath) {
		String newXPath = "";
		
		// TODO: Handle an xpath that is an array
		int endOfXPath = xpath.lastIndexOf(']');
		
		// if the xpath doesn't have properties already, then start some
		if (endOfXPath == -1) {
			newXPath = xpath + "[";
		} else {
			newXPath = xpath.substring(0, endOfXPath) + " and ";
		}
		
		newXPath = newXPath + "@visible='true']";
		return newXPath;
	}
	
	public static String addLabel(String xpath, String label) {
		String newXPath = "";
		
		// TODO: Handle an xpath that is an array
		int endOfXPath = xpath.lastIndexOf(']');
		
		// if the xpath doesn't have properties already, then start some
		if (endOfXPath == -1) {
			newXPath = xpath + "[";
		} else {
			newXPath = xpath.substring(0, endOfXPath) + " and ";
		}
		
		newXPath = newXPath + textXPath(label) + "]";
		
		return newXPath;
	}

	public static String addResourceId(String xpath, String resourceId) {
		String newXPath = "";
		
		// TODO: Handle an xpath that is an array
		int endOfXPath = xpath.lastIndexOf(']');
		
		// if the xpath doesn't have properties already, then start some
		if (endOfXPath == -1) {
			newXPath = xpath + "[";
		} else {
			newXPath = xpath.substring(0, endOfXPath) + " and ";
		}
				
		if (Device.isIOS()) {
			newXPath = newXPath + "@name='" + resourceId + "']";
		} else {
			newXPath = newXPath + "contains(@resource-id, '" + resourceId + "')]";
		}
		
		return newXPath;
	}	

	public static String addChildWithType(String xpath, String type) {
		String newXPath = xpath;
		if (Device.isIOS()) {
			newXPath = newXPath + "/*[@type='" + type + "']";
		} else {
			newXPath = newXPath + "/*[contains(@class, '" + type + "')]";
		}
		
		return newXPath;
	}

	/**
	 * Adds an index qualifier to an existing xpath
	 * @param xpath - existing xpath
	 * @param index - index to look for. NOTE: xpath indexes start at 1.
	 * @return new xpath with index appended
	 */
	public static String addIndex(String xpath, Integer index) {
		String newXPath = xpath;
		newXPath = "(" + xpath + ")[" + index + "]";
		return newXPath;
	}
	
	private static String textXPath(String labelText) {
		// xpath 1 doesn't have a way to ignore case, so we need to force-lowercase both strings
		if (Device.isIOS()) {
			return "starts-with(normalize-space(translate(@label, " + upperCase + ", " + lowerCase + ")), \"" + labelText.toLowerCase() + "\")";
		} else {
			return "starts-with(normalize-space(translate(@text, " + upperCase + ", " + lowerCase + ")), \"" + labelText.toLowerCase() + "\")";
		}
	}	
	
}
