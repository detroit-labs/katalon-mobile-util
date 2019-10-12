package com.detroitlabs.katalonmobileutil.testobject;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileComponent;
import com.detroitlabs.katalonmobileutil.component.mobile.MobileTextField;
import com.detroitlabs.katalonmobileutil.component.mobile.android.AndroidTextField;
import com.detroitlabs.katalonmobileutil.component.mobile.ios.IOSTextField;
import com.detroitlabs.katalonmobileutil.component.web.WebTextField;
import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

// TODO: Switch class to use generics for type
public class TextField {

    private com.detroitlabs.katalonmobileutil.component.TextField textField;
    private TestObject testObject;

    public TextField(TestObject testObject) {
    	this.testObject = testObject;
        if (Device.isIOS()) {
            textField = new IOSTextField(testObject);
        } else if (Device.isAndroid()) {
            textField = new AndroidTextField(testObject);
        } else {
            textField = new WebTextField(testObject);
        }
    }

	public static void clearText(TestObject field, Integer timeout) {
		TextField.clearText(field, timeout, null);
	}

	/**
	 * Clears the text out of a field.
	 * @deprecated Use the instance method for clearText(timeout, clearButton).
	 * @param field TestObject of the text field to clear
	 * @param timeout seconds to wait to see if the clearing finished before throwing an error
	 * @param clearButton Reqiured by Android only. Button that triggers a text field to clear.
	 */
	public static void clearText(TestObject field, Integer timeout, TestObject clearButton) {
		(new TextField(field)).clearText(timeout, clearButton);
	}

	/**
	 * Clears the text out of a field.
	 * @param timeout seconds to wait to see if the clearing finished before throwing an error
	 * @param clearButton Reqiured by Android only. Button that triggers a text field to clear.
	 */
	public void clearText(Integer timeout, TestObject clearButton) {
        if (Device.isIOS()) {
            ((IOSTextField)textField).clearText(timeout);
        } else if (Device.isAndroid()) {
			((AndroidTextField)textField).clearText(timeout, clearButton);
        } else {
			((WebTextField)textField).clearText();
        }
    }

	/** Type text at the current cursor position. Requires that a TextField is already active and the keyboard is visible.
	 * @deprecated Use the instance method for type(text).
	 * @param text text to type into the field.
	 */
	public static void typeText(String text) {
        MobileComponent.typeTextOnKeyboard(text);
	}

    /** Type text at the current cursor position. Requires that a TextField is already active and the keyboard is visible.
     * @param text text to type into the field.
     */
	public void type(String text) {
		MobileComponent.typeTextOnKeyboard(text);
    }

	/** Type text in a specific TextField. Does not require the TextField to have focus already.
	 * @deprecated Use the instance method for type(text, timeout)
	 * @param field TextField object to receive the text input.
	 * @param text text to type into the field.
	 * @param timeout timeout (in seconds) for TextField-related actions.
	 */
	public static void typeText(TestObject field, String text, Integer timeout) {
        (new TextField(field)).typeText(text, timeout);
	}

    /** Type text in this TextField. Does not require the TextField to have focus already.
     * @param text text to type into the field.
     * @param timeout timeout (in seconds) for TextField-related actions.
     */
    public void typeText(String text, Integer timeout) {
		textField.typeText(text, timeout);
    }
	
	/** Choose a single value from a drop-down or picker. 
	 * @deprecated Use the instance method for selectOption(pickerChoice, timeout).
	 * @param field TextField object which will trigger and receive the input from the picker.
	 * @param pickerChoice value of the picker to select.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public static void selectOption(TestObject field, String pickerChoice, Integer timeout) throws NoSuchPickerChoiceException {
		(new TextField(field)).selectOption(pickerChoice, timeout);
	}

	/** Choose a single value from a drop-down or picker.
	 * @param pickerChoice value of the picker to select.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public void selectOption(String pickerChoice, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<>();
		pickerChoices.add(pickerChoice);
		// Assume that the picker choice is also the string that will be used to validate the selection.
		selectOption(pickerChoices, pickerChoice, timeout);
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

	/** Choose a single value from a drop-down or picker. Use this in cases where the picker value is transformed before
	 * showing in the text field.
	 * @param pickerChoice value of the picker to select.
	 * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public void selectOption(String pickerChoice, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<>();
		pickerChoices.add(pickerChoice);
		selectOption(pickerChoices, expectedFieldValue, timeout);
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

		new TextField(field).selectOption(pickerChoices, expectedFieldValue, timeout);
		
	}

	/**
	 * Select multiple values from a multipart picker.
	 * @param pickerChoices value of the option to select for each part of the picker.
	 * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public void selectOption(List<String> pickerChoices, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {

		// Open the picker by tapping on the field that triggers it
		MobileBuiltInKeywords.tap(this.testObject, timeout);

		// Sometimes there will be multiple pickers side-by-side, e.g. multipart date formats for month, date, year
		for (int i=0; i < pickerChoices.size(); i++) {
			String pickerChoice = pickerChoices.get(i);

			if (Device.isIOS()) {
				((IOSTextField)textField).selectOption(pickerChoice, expectedFieldValue, timeout);
			} else if (Device.isAndroid()) {
				((AndroidTextField)textField).selectOption(pickerChoice, expectedFieldValue, timeout);
			} else {
				throw new NotImplementedException("selectOption not implemented for Web.");
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
