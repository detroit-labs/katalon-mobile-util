package com.detroitlabs.katalonmobileutil.testobject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.interactions.Keyboard;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
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
	
	/** Choose a value from a drop-down or picker. 
	 * Automatically closes the picker once the selection is made.
	 * 
	 * @param field Text field TestObject that triggers the drop-down
	 * @param pickerChoice Value of the picker to select
	 * @param timeout Timeout (in seconds) for picker-related actions
	 */
	public static void selectOption(TestObject field, String pickerChoice, Integer timeout) {
		List<String> pickerChoices = new ArrayList<String>();
		pickerChoices.add(pickerChoice);
		TextField.selectOption(field, pickerChoices, timeout);
	}	
	
	public static void selectOption(TestObject field, List<String> pickerChoices, Integer timeout) {
		MobileBuiltInKeywords.tap(field, timeout);

		if (Device.isIOS()) {
			
			// Sometimes there will be multiple pickers side-by-side, e.g. multipart date formats for month, date, year
			for (int i=0; i < pickerChoices.size(); i++) {
				// Find the picker wheel and set its text, which spins the wheel
				TestObject pickerWheel = new TestObject();
				pickerWheel.addProperty("xpath", ConditionType.EQUALS, "(//XCUIElementTypePickerWheel)[" + (i + 1) + "]");
				MobileBuiltInKeywords.setText(pickerWheel, pickerChoices.get(i), timeout);
				Logger.debug("Current pickerWheel value: " + MobileBuiltInKeywords.getText(pickerWheel, timeout));
			}
			
			// Find the SELECT or DONE button on the picker wheel toolbar and tap it, applying the value to the field
			TestObject selectButton = new TestObject();
			// Xpath 1 (used by Selenium) doesn't have matches, so this is a substitute
			selectButton.addProperty("xpath", ConditionType.EQUALS, "//XCUIElementTypeButton[@name='SELECT' or @name='DONE']");
			Logger.debug("Tapping the button to close the picker:");
			Logger.printTestObject(selectButton, LogLevel.DEBUG);
			MobileBuiltInKeywords.tap(selectButton, timeout);
			
			// TODO: Verify that the value selected was actually set. This can be tricky if the text of the picker gets transformed
			// in code when selected, e.g. October -> 10
			// It is possible to try to set the text of a field to something not in the picker list, in which case it fails silently.
			// We need to verify that the selection was made correctly, or throw an exception.
//			String newTextFieldValue = MobileBuiltInKeywords.getText(field, timeout);
//			if (!pickerChoice.equals(newTextFieldValue)) {
//				throw(new NoSuchPickerChoiceException(pickerChoice));
//			}
			
		} else {
			// For Android, the picker is a scrolling list of labels
//			Scroll.scrollListToElementWithText(pickerChoice);
//			TestObject selection = Finder.findLabelWithText(pickerChoice);
//			MobileBuiltInKeywords.tap(selection, timeout);
		}
	}

}
