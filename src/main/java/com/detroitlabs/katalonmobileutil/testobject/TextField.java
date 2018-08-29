package com.detroitlabs.katalonmobileutil.testobject;

import org.openqa.selenium.interactions.Keyboard;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.detroitlabs.katalonmobileutil.touch.Scroll;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class TextField {
	
	public static void clearText(TestObject field, Integer timeout) {
		TextField.clearText(field, timeout, null);
	}
	
	public static void clearText(TestObject field, Integer timeout, TestObject clearButton) {
		
		if (Device.isIOS()) {
			// iOS identifies the text fields directly with accessibilityIds.
			// The iOS text field can be cleared directly.
			MobileBuiltInKeywords.clearText(field, timeout);
		} else {
			// Android wraps the text fields in RelativeLayouts, which in turn get the resource-id.
			
			// First tap the field to activate it
			MobileBuiltInKeywords.tap(field, timeout);
			
			// Press the clear button, if it was provided
			if (clearButton != null) {
				MobileBuiltInKeywords.tap(clearButton, timeout, FailureHandling.OPTIONAL);
			}
		}
	}
	
	public static void typeText(TestObject field, String text, Integer timeout) {
				
		if (Device.isIOS()) {
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
			@SuppressWarnings("unchecked")
			AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
			Keyboard keyboard = driver.getKeyboard();
			keyboard.sendKeys(text);
			
		}
	}
	
	/** Choose a value from a drop-down or picker
	 * 
	 * @param field Text field TestObject that triggers the drop-down
	 * @param pickerChoice Value of the picker to select
	 * @param timeout Timeout (in seconds) for picker-related actions
	 */
	public static void selectOption(TestObject field, String pickerChoice, Integer timeout) {
		
		MobileBuiltInKeywords.tap(field, timeout);

		if (Device.isIOS()) {
			// Find the picker wheel and set its text, which spins the wheel
			TestObject pickerWheel = new TestObject();
			pickerWheel.addProperty("type", ConditionType.EQUALS, "XCUIElementTypePickerWheel");
			MobileBuiltInKeywords.setText(pickerWheel, pickerChoice, timeout);
			
			// Find the SELECT button on the picker wheel toolbar and tap it, applying the value to the field
			TestObject selectButton = new TestObject();
			selectButton.addProperty("type", ConditionType.EQUALS, "XCUIElementTypeButton");
			selectButton.addProperty("name", ConditionType.EQUALS, "SELECT");
			MobileBuiltInKeywords.tap(selectButton, timeout);
		} else {
			// For Android, the picker is a scrolling list of labels
			Scroll.scrollListToElementWithText(pickerChoice);
			TestObject selection = Finder.findLabelWithText(pickerChoice);
			MobileBuiltInKeywords.tap(selection, timeout);
		}
	}
}
