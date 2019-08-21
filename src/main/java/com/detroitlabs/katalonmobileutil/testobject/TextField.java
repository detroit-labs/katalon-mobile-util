package com.detroitlabs.katalonmobileutil.testobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.NoSuchElementException;
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
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class TextField {
	
	public static void clearText(TestObject field, Integer timeout) {
		TextField.clearText(field, timeout, null);
	}
	
	public static void clearText(TestObject field, Integer timeout, TestObject clearButton) {

		if (Device.isWeb()) {
			WebUiBuiltInKeywords.clearText(field);
		}
		else if (Device.isIOS()) {
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
	
	/** Type text at the current cursor position. Requires that a TextField is already active and the keyboard is visible.
	 * 
	 * @param text text to type into the field.
	 */
	public static void typeText(String text) {
			
		// Using the keyboard API, send all of the keys (luckily, this doesn't need to be done one at a time).
		@SuppressWarnings("unchecked")
		AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
		Keyboard keyboard = driver.getKeyboard();
		keyboard.sendKeys(text);
		
	}	
	
	/** Type text in a specific TextField.
	 * 
	 * @param field TextField object to receive the text input.
	 * @param text text to type into the field.
	 * @param timeout timeout (in seconds) for TextField-related actions.
	 */
	public static void typeText(TestObject field, String text, Integer timeout) {
				
		if (Device.isWeb()) {
			WebUiBuiltInKeywords.sendKeys(field, text);
		}
		else if (Device.isIOS()) {
			// iOS identifies the text fields directly with accessibilityIds.
			// Text can be set directly on the iOS field.
			MobileBuiltInKeywords.setText(field, text, timeout);
		} else {
			// Android wraps the text fields in RelativeLayouts, which in turn get the resource-id.
			// However, we can't set the text directly on the RelativeLayout, nor can we get to the EditText
			// field within it.
			
			// First tap the field to activate it, bringing up the keyboard
			MobileBuiltInKeywords.tap(field, timeout);
			
			TextField.typeText(text);
			
		}
	}
	
	/** Choose a single value from a drop-down or picker. 
	 * 
	 * @param field TextField object which will trigger and receive the input from the picker.
	 * @param pickerChoice value of the picker to select.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public static void selectOption(TestObject field, String pickerChoice, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<String>();
		pickerChoices.add(pickerChoice);
		// Assume that the picker choice is also the string that will be used to validate the selection.
		TextField.selectOption(field, pickerChoices, pickerChoice, timeout);
	}	
	
	/** Choose a single value from a drop-down or picker. Use this in cases where the picker value is transformed before
	 * showing in the text field. 
	 * 
	 * @param field TextField object which will trigger and receive the input from the picker.
	 * @param pickerChoice value of the picker to select.
	 * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public static void selectOption(TestObject field, String pickerChoice, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<String>();
		pickerChoices.add(pickerChoice);
		TextField.selectOption(field, pickerChoices, expectedFieldValue, timeout);
	}	
	
	/**
	 * Select multiple values from a multipart picker.
	 * @param field TextField object which will trigger and receive the input from the picker.
	 * @param pickerChoices value of the option to select for each part of the picker.
	 * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public static void selectOption(TestObject field, List<String> pickerChoices, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {
		
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
					// TODO: What if there are multiple lists ?
					// Not a real picker, just a list of text elements
					selectOptionFromAndroidTextList(pickerChoice, timeout); 
				}

			}
			
		}
		
		// Find the SELECT, DONE, or OK button on the picker wheel and tap it, applying the value to the field
		TextField.tapButtonWithText(Arrays.asList("OK", "SELECT", "Select", "DONE", "Done"));
		
		// It is possible to try to set the text of a field to something not in the picker list, in which case it fails silently.
		// We need to verify that the selection was made correctly, or throw an exception.
		
		String newTextFieldValue = getPickerValue(field, timeout);
		Logger.debug("Verifying that the picker value(s) of: " + pickerChoices + " was set correctly on " + field + " which now has a value of: " + newTextFieldValue);
		Logger.debug("Picker value set successfully!");
		if (!expectedFieldValue.equalsIgnoreCase(newTextFieldValue)) {
			throw(new NoSuchPickerChoiceException(pickerChoices));
		}
		
	}
	
	private static String getPickerValue(TestObject field, Integer timeout) {
		String pickerValue = null;
		if (Device.isIOS()) {
			pickerValue = MobileBuiltInKeywords.getText(field, timeout);
		} else {
			String type = TestObjectConverter.typeFromTestObject(field);
			
			String xpath = XPathBuilder.createXPath(type);
				
			String resourceId = TestObjectConverter.resourceIdFromTestObject(field);
			xpath = XPathBuilder.addResourceId(xpath, resourceId);
			
			// Android spinners don't expose their text - we have to dig in to get the TextView 
			if (type.contains("Spinner")) {
				xpath = XPathBuilder.addChildWithType(xpath, "TextView");
			}
			
			TestObject spinner = new TestObject();
			spinner.addProperty("xpath", ConditionType.EQUALS, xpath);
			
			pickerValue = MobileBuiltInKeywords.getText(spinner, timeout);
			
		}
		
		return pickerValue;
	}
	
	/**
	 * Moves the keyboard focus to the next field in the form
	 */
	public static void nextField() {
		// TODO: Check if keyboard is open?
		if (Device.isIOS()) {
			// For iOS, look at the buttons and find one that matches "Next", but there can be a few variations
			tapButtonWithText(Arrays.asList("NEXT", "Next", ">"));
		} else {
			// For Android, we can use the Tab key
			AndroidDriver<?> driver = (AndroidDriver<?>) MobileDriverFactory.getDriver();
			driver.pressKey(new KeyEvent(AndroidKey.TAB));
		}
	}
	
	public static void hideKeyboard() {
		AppiumDriver<?> driver = (AppiumDriver<?>) MobileDriverFactory.getDriver();
		if (driver.getKeyboard() != null) {
			if (Device.isIOS()) {
				// The hideKeyboard function often crashes in iOS.
				// Find the DONE button on the keyboard toolbar and tap it, closing the keyboard
				tapButtonWithText(Arrays.asList("DONE", "Done"));
			} else {
				// Android's hideKeyboard seems more reliable.
				driver.hideKeyboard();
			}	
		} else {
			Logger.debug("Keyboard is already hidden.");
		}
		
	}
	
	private static void tapButtonWithText(List<String> names) {
		if (Device.isIOS()) {
			tapButtonWithText(names, "XCUIElementTypeButton", "name");
		} else {
			tapButtonWithText(names, "android.widget.Button", "text");
		}
	}
	
	private static void tapButtonWithText(List<String>names, String objectClass, String objectProperty) {
		TestObject button = new TestObject();
		// Xpath 1 (used by Selenium) doesn't have matches, so this is a substitute
		StringBuilder sb = new StringBuilder();
		sb.append("@").append(objectProperty).append("='").append(names.get(0)).append("'");
		for (int i=1; i<names.size(); i++) {
			sb.append(" or @").append(objectProperty).append("='").append(names.get(i)).append("'");
		}
		button.addProperty("xpath", ConditionType.EQUALS, "//" + objectClass + "[" + sb.toString() + "]");
		Logger.debug("Tapping the button to close the keyboard or picker:");
		Logger.printTestObject(button, LogLevel.DEBUG);
		MobileBuiltInKeywords.tap(button, 1, FailureHandling.OPTIONAL);
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
		MobileBuiltInKeywords.tapAndHold(picker, timeout / 1000, timeout);
		
		// Setting the text will spin the picker
		// In order to work consistently, we must set the value twice, once with the keyboard and once directly on the field.
		driver.getKeyboard().sendKeys(pickerChoice);
		MobileElement p = (MobileElement) driver.findElementByXPath(pickerXPath);
		p.setValue(pickerChoice);
		
		// In order for the picker change to take effect, need to apply it to the field
		driver.pressKey(new KeyEvent(AndroidKey.ENTER));
		
	}
	
	private static void selectOptionFromAndroidTextList(String pickerChoice, Integer timeout) {
		// When the "picker" is just a list of labels
		Scroll.scrollListToElementWithText(pickerChoice, timeout);
		TestObject selection = Finder.findLabelWithText(pickerChoice);
		MobileBuiltInKeywords.tap(selection, timeout);
	}
	
	private static void selectOptionFromIOSPicker(int pickerIndex, String pickerChoice, int timeout) {
		// Find the picker wheel and set its text, which spins the wheel
		TestObject pickerWheel = new TestObject();
		pickerWheel.addProperty("xpath", ConditionType.EQUALS, "(//XCUIElementTypePickerWheel)[" + (pickerIndex + 1) + "]");
		MobileBuiltInKeywords.sendKeys(pickerWheel, pickerChoice);
		Logger.debug("Current pickerWheel value: " + MobileBuiltInKeywords.getText(pickerWheel, timeout));
		
	}

}
