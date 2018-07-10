package com.detroitlabs.katalonsupport.formfield;

import org.openqa.selenium.interactions.Keyboard;

import com.detroitlabs.katalonsupport.device.Device;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class FormField {

	AppiumDriver<MobileElement> driver;

	@SuppressWarnings("unchecked")
	FormField() {
		this.driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
	}
	
	public void clearText(TestObject field, int timeout) {
		this.clearText(field, timeout, null);
	}
	
	public void clearText(TestObject field, int timeout, TestObject clearButton) {
		Device device = new Device();
		
		if (device.isIOS()) {
			// iOS identifies the text fields directly with accessibilityIds.
			// The iOS text field can be cleared directly.
			MobileBuiltInKeywords.clearText(field, timeout);
		} else {
			// Android wraps the text fields in RelativeLayouts, which in turn get the resource-id.
			
			// First tap the field to activate it
			MobileBuiltInKeywords.tap(field, timeout);
			
			// Press the clear button, if it was provided
			if (clearButton != null) {
				MobileBuiltInKeywords.tap(clearButton, timeout);
			}
		}
	}
	
	public void typeText(TestObject field, String text, int timeout) {
		
		Device device = new Device();
		
		if (device.isIOS()) {
			// iOS identifies the text fields directly with accessibilityIds.
			// Text can be set directly on the iOS field.
			MobileBuiltInKeywords.setText(field, text, timeout);
		} else {
			// Android wraps the text fields in RelativeLayouts, which in turn get the resource-id.
			// However, we can't set the text directly on the RelativeLayout, nor can we get to the EditText
			// field within it.
			
			// First tap the field to activate it, bringing up the keyboard
			MobileBuiltInKeywords.tap(field, timeout);
			
			// Using the keyboard API, send all of the keys (luckily, this doesn't need to be done one at a time).
			Keyboard keyboard = this.driver.getKeyboard();
			keyboard.sendKeys(text);
			
		}
	}
}
