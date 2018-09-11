package com.detroitlabs.katalonmobileutil.touch;

import com.detroitlabs.katalonmobileutil.device.Device;

public class Scroll {

	/** 
	 * Scrolls through a list of all text elements on the screen, attempting to find the requested text.
	 * Throws an exception if the text is not found.
	 * Android only. For iOS, use scrollListToElementWithText(String elementId, String elementText)
	 * @param elementText text to attempt to find in the scrolling list.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementText) {
		if (Device.isIOS()) {
			throw new UnsupportedOperationException("Parameters elementId and elementText are required for iOS.");
		} 
		
		if (Device.isAndroid()) {
			return ScrollAndroid.scrollListToElementWithText(elementText);
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
	public static boolean scrollListToElementWithText(String elementId, String elementText) {
		if (Device.isIOS()) {
			return ScrollIOS.scrollListToElementWithText(elementId, elementText);
		} 
		
		if (Device.isAndroid()) {
			return ScrollAndroid.scrollListToElementWithText(elementId, elementText);
		}
		
		throw new UnsupportedOperationException("Device type is not supported.");
	}
	
}
