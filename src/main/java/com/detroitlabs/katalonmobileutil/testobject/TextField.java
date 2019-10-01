package com.detroitlabs.katalonmobileutil.testobject;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileComponent;
import com.detroitlabs.katalonmobileutil.component.mobile.android.AndroidTextField;
import com.detroitlabs.katalonmobileutil.component.mobile.ios.IOSTextField;
import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

import java.util.ArrayList;
import java.util.List;

public class TextField {
	
	public static void clearText(TestObject field, Integer timeout) {
		TextField.clearText(field, timeout, null);
	}

	/**
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 * @param field
	 * @param timeout
	 * @param clearButton
	 */
	public static void clearText(TestObject field, Integer timeout, TestObject clearButton) {

		if (Device.isWeb()) {
			WebUiBuiltInKeywords.clearText(field);
		}
		else if (Device.isIOS()) {
			new IOSTextField(field).clearText(timeout, clearButton);
		} else {
			new AndroidTextField(field).clearText(timeout, clearButton);
		}
	}
	
	/** Type text at the current cursor position. Requires that a TextField is already active and the keyboard is visible.
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 * @param text text to type into the field.
	 */
	public static void typeText(String text) {
		MobileComponent.typeTextOnKeyboard(text);
	}
	
	/** Type text in a specific TextField.
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 * @param field TextField object to receive the text input.
	 * @param text text to type into the field.
	 * @param timeout timeout (in seconds) for TextField-related actions.
	 */
	public static void typeText(TestObject field, String text, Integer timeout) {
				
		if (Device.isWeb()) {
			WebUiBuiltInKeywords.sendKeys(field, text);
		}
		else if (Device.isIOS()) {
			new IOSTextField(field).typeText(text, timeout);
		} else {
			new AndroidTextField(field).typeText(text, timeout);
		}
	}
	
	/** Choose a single value from a drop-down or picker. 
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 * @param field TextField object which will trigger and receive the input from the picker.
	 * @param pickerChoice value of the picker to select.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public static void selectOption(TestObject field, String pickerChoice, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<>();
		pickerChoices.add(pickerChoice);
		// Assume that the picker choice is also the string that will be used to validate the selection.
		TextField.selectOption(field, pickerChoices, pickerChoice, timeout);
	}	
	
	/** Choose a single value from a drop-down or picker. Use this in cases where the picker value is transformed before
	 * showing in the text field. 
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 * @param field TextField object which will trigger and receive the input from the picker.
	 * @param pickerChoice value of the picker to select.
	 * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public static void selectOption(TestObject field, String pickerChoice, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<>();
		pickerChoices.add(pickerChoice);
		TextField.selectOption(field, pickerChoices, expectedFieldValue, timeout);
	}	
	
	/**
	 * Select multiple values from a multipart picker.
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
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
				new IOSTextField(field).selectOption(pickerChoice, expectedFieldValue, timeout);
			} else {
				new AndroidTextField(field).selectOption(pickerChoice, expectedFieldValue, timeout);
			}
			
		}
		
	}
	
	/**
	 * Moves the keyboard focus to the next field in the form
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 */
	public static void nextField() {
		// TODO: Check if keyboard is open?
		if (Device.isIOS()) {
			new IOSTextField(null).nextField();
		} else {
			new AndroidTextField(null).nextField();
		}
	}

	/**
	 * @deprecated Use the AndroidTextField or IOSTextField classes to perform this function.
	 */
	public static void hideKeyboard() {
		if (Device.isIOS()) {
			new IOSTextField(null).hideKeyboard();
		} else {
			new AndroidTextField(null).hideKeyboard();
		}
	}

}
