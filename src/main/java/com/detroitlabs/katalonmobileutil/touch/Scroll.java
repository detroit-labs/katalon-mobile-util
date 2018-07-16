package com.detroitlabs.katalonmobileutil.touch;

import com.detroitlabs.katalonmobileutil.device.Device;

public class Scroll {

	public static boolean scrollListToElementWithText(String elementText) {
		if (Device.isIOS()) {
			throw new UnsupportedOperationException("Parameters elementId and elementText are required for iOS.");
		} 
		
		if (Device.isAndroid()) {
			return ScrollAndroid.scrollListToElementWithText(elementText);
		}
		
		throw new UnsupportedOperationException("Device type is not supported.");
	}
	
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
