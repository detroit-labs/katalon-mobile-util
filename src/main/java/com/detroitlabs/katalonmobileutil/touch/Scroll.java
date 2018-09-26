package com.detroitlabs.katalonmobileutil.touch;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.kms.katalon.core.testobject.TestObject;

public class Scroll {

	/**
	 * Scrolls through a list of all text elements on the screen, attempting to find
	 * the requested text. Throws an exception if the text is not found. Android
	 * only. For iOS, use scrollListToElementWithText(String elementId, String
	 * elementText)
	 * 
	 * @param elementText
	 *            text to attempt to find in the scrolling list.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementText, Integer timeout) {
		if (Device.isIOS()) {
			// TODO: allow this for iOS?
			throw new UnsupportedOperationException("Parameters elementId and elementText are required for iOS.");
		}

		if (Device.isAndroid()) {
			return ScrollAndroid.scrollListToElementWithText(elementText, timeout);
		}

		throw new UnsupportedOperationException("Device type is not supported.");
	}
	
	/**
	 * Scrolls through a list of all checkbox elements on the screen, attempting to find
	 * the requested text. Throws an exception if the text is not found. Android
	 * only. For iOS, use scrollListToElementWithText(String elementId, String
	 * elementText)
	 * 
	 * @param elementText
	 *            text to attempt to find in the scrolling list.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToCheckboxWithText(String elementText, Integer timeout) {
		if (Device.isIOS()) {
			// TODO: allow this for iOS?
			throw new UnsupportedOperationException("Parameters elementId and elementText are required for iOS.");
		}

		if (Device.isAndroid()) {
			return ScrollAndroid.scrollListToCheckboxWithText(elementText, timeout);
		}

		throw new UnsupportedOperationException("Device type is not supported.");
	}	

	/**
	 * Scrolls through a list of a specific collection of elements on the screen, attempting to find the requested text.
	 * Throws an exception if the text is not found. 
	 * @param elementId identifier of the collection of text elements to scroll (Accessibility id/name for iOS and resource-id for Android).
	 * @param elementText text to attempt to find in the scrolling list.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementId, String elementText, Integer timeout) {
		if (Device.isIOS()) {
			return ScrollIOS.scrollListToElementWithText(elementId, elementText);
		} 
		
		if (Device.isAndroid()) {
			return ScrollAndroid.scrollListToElementWithText(elementId, elementText, timeout);
		}
		
		throw new UnsupportedOperationException("Device type is not supported.");
	}

	/**
	 * Scrolls through a list of a specific collection of elements on the screen, attempting to find the requested text.
	 * Throws an exception if the text is not found. 
	 * @param testObject template TestObject to use to find a collection of similar elements. Will use the class/type and accessibility/resource id if available.
	 * @param elementText text to attempt to find in the scrolling list.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(TestObject testObject, String elementText, Integer timeout) {
		if (Device.isIOS()) {
			return ScrollIOS.scrollListToElementWithText(testObject, elementText);
		} 
		
		if (Device.isAndroid()) {
			// TODO: reactivate
//			return ScrollAndroid.scrollListToElementWithText(testObject, elementText);
			return false;
		}
		
		throw new UnsupportedOperationException("Device type is not supported.");
	}
	
}
