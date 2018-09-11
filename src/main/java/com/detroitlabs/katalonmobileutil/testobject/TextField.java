package com.detroitlabs.katalonmobileutil.testobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
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
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

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
		
		// Open the picker by tapping on the field that triggers it
		MobileBuiltInKeywords.tap(field, timeout);

		// Sometimes there will be multiple pickers side-by-side, e.g. multipart date formats for month, date, year
		for (int i=0; i < pickerChoices.size(); i++) {
			String pickerChoice = pickerChoices.get(i);
		
			if (Device.isIOS()) {
				
				selectOptionFromIOSPicker(i, pickerChoices.get(i), timeout);
				
			} else {
						
				 	// Android
					try {
						selectOptionFromAndroidPicker(i, pickerChoice, timeout);
					} catch (NoSuchElementException ex) {
						// TODO: How to handle multiple pickers?
						// Not a real picker, just a list of text elements
						selectOptionFromAndroidTextList(pickerChoice, timeout); 
					}

			}
			
		}
		
		if (Device.isIOS()) {
			// Find the SELECT or DONE button on the picker wheel toolbar and tap it, applying the value to the field
			TextField.tapButtonWithNameIn(Arrays.asList("SELECT", "Select", "DONE", "Done"));
		}
		
		// TODO: Verify that the value selected was actually set. This can be tricky if the text of the picker gets transformed
		// in code when selected, e.g. October -> 10
		// It is possible to try to set the text of a field to something not in the picker list, in which case it fails silently.
		// We need to verify that the selection was made correctly, or throw an exception.
//		String newTextFieldValue = MobileBuiltInKeywords.getText(field, timeout);
//		if (!pickerChoice.equals(newTextFieldValue)) {
//			throw(new NoSuchPickerChoiceException(pickerChoice));
//		}
		
	}
	
	/**
	 * Moves the keyboard focus to the next field in the form
	 */
	public static void nextField() {
		// TODO: Check if keyboard is open?
		if (Device.isIOS()) {
			// For iOS, look at the buttons and find one that matches "Next", but there can be a few variations
			TextField.tapButtonWithNameIn(Arrays.asList("NEXT", "Next", ">"));
		} else {
			// For Android, we can use the Tab key
			AndroidDriver<?> driver = (AndroidDriver<?>) MobileDriverFactory.getDriver();
			driver.pressKeyCode(AndroidKeyCode.KEYCODE_TAB);
		}
	}
	
	public static void hideKeyboard() {
		AppiumDriver<?> driver = (AppiumDriver<?>) MobileDriverFactory.getDriver();
		if (driver.getKeyboard() != null) {
			if (Device.isIOS()) {
				// The hideKeyboard function often crashes in iOS.
				// Find the DONE button on the keyboard toolbar and tap it, closing the keyboard
				TextField.tapButtonWithNameIn(Arrays.asList("DONE", "Done"));
			} else {
				// Android's hideKeyboard seems more reliable.
				driver.hideKeyboard();
			}	
		} else {
			Logger.debug("Keyboard is already hidden.");
		}
		
	}
	
	private static void tapButtonWithNameIn(List<String> names) {
		TestObject button = new TestObject();
		// Xpath 1 (used by Selenium) doesn't have matches, so this is a substitute
		StringBuilder sb = new StringBuilder();
		sb.append("@name='").append(names.get(0)).append("'");
		for (int i=1; i<names.size(); i++) {
			sb.append(" or @name='").append(names.get(i)).append("'");
		}
		button.addProperty("xpath", ConditionType.EQUALS, "//XCUIElementTypeButton[" + sb.toString() + "]");
		Logger.debug("Tapping the button to close the keyboard or picker:");
		Logger.printTestObject(button, LogLevel.DEBUG);
		MobileBuiltInKeywords.tap(button, 0);
	}
	
	private static void selectOptionFromAndroidPicker(int pickerIndex, String pickerChoice, int timeout) throws NoSuchElementException {
		
		// When the picker is an actual picker, find the picker wheel's EditText node and set its text, which spins the wheel
		
		AndroidDriver<?> driver = (AndroidDriver<?>) MobileDriverFactory.getDriver();	
		
		// Find the picker at the given index (xpath indexes start at 1)
		String pickerXPath = "(//android.widget.NumberPicker)[" + (pickerIndex + 1) + "]/android.widget.EditText";
		
		driver.findElementByXPath(pickerXPath);

		TestObject picker = new TestObject();
		picker.addProperty("xpath", ConditionType.EQUALS, pickerXPath);
		
		// Need to activate the keyboard first
		MobileBuiltInKeywords.tap(picker, timeout);
		
		// Setting the text will spin the picker
		MobileBuiltInKeywords.setText(picker, pickerChoice, timeout);
		
		// In order for the picker change to take effect, need to apply it to the field
		driver.pressKeyCode(AndroidKeyCode.ENTER);
		
	}
	
	private static void selectOptionFromAndroidTextList(String pickerChoice, int timeout) {
		// When the "picker" is just a list of labels
		Scroll.scrollListToElementWithText(pickerChoice);
		TestObject selection = Finder.findLabelWithText(pickerChoice);
		MobileBuiltInKeywords.tap(selection, timeout);
	}
	
	private static void selectOptionFromIOSPicker(int pickerIndex, String pickerChoice, int timeout) {
		// Find the picker wheel and set its text, which spins the wheel
		TestObject pickerWheel = new TestObject();
		pickerWheel.addProperty("xpath", ConditionType.EQUALS, "(//XCUIElementTypePickerWheel)[" + (pickerIndex + 1) + "]");
//		MobileBuiltInKeywords.setText(pickerWheel, pickerChoice, timeout);
		MobileBuiltInKeywords.sendKeys(pickerWheel, pickerChoice);
		Logger.debug("Current pickerWheel value: " + MobileBuiltInKeywords.getText(pickerWheel, timeout));
		
	}

}
