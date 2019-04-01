package com.detroitlabs.katalonmobileutil.component.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.detroitlabs.katalonmobileutil.component.Component;
import com.detroitlabs.katalonmobileutil.component.TextField;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;

import org.openqa.selenium.interactions.Keyboard;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * MobileTextField
 */
public abstract class MobileTextField extends MobileComponent implements TextField {

	public MobileTextField(TestObject field) {
		super(field);
	}

	public abstract void clearText(Integer timeout, TestObject clearButton);
    /** Type text in the TextField.
	 * 
	 * @param text text to type into the field.
	 * @param timeout timeout (in seconds) for TextField-related actions.
	 */
    public abstract void typeText(String text, Integer timeout);
    /**
	 * Moves the keyboard focus to the next field in the form
	 */
    public abstract void nextField();
    public abstract void selectOptionFromPicker(int pickerIndex, String pickerChoice, int timeout);
    public abstract String getPickerValue(Integer timeout);
	public abstract void tapButtonWithText(List<String> names);

	public static void tapButtonWithText(List<String>names, String objectClass, String objectProperty) {
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

	public static void typeTextOnKeyboard(String text) {
		// Using the keyboard API, send all of the keys (luckily, this doesn't need to be done one at a time).
		@SuppressWarnings("unchecked")
		AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver();
		Keyboard keyboard = driver.getKeyboard();
		keyboard.sendKeys(text);
	}

	@Override
	public void clearText() {
		clearText(Component.defaultTimeout);
	}

	public void clearText(Integer timeout) {
		clearText(timeout, null);
	}
	
	/** Type text at the current cursor position. Requires that a TextField is already active and the keyboard is visible.
	 * 
	 * @param text text to type into the field.
	 */
	@Override
	public void typeText(String text) {
		typeText(text, defaultTimeout);
	}
	
	/** Choose a single value from a drop-down or picker. 
	 * 
	 * @param pickerChoice value of the picker to select.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public void selectOption(String pickerChoice, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<String>();
		pickerChoices.add(pickerChoice);
		// Assume that the picker choice is also the string that will be used to validate the selection.
		selectOption(pickerChoices, pickerChoice, timeout);
	}	
	
	/** Choose a single value from a drop-down or picker. Use this in cases where the picker value is transformed before
	 * showing in the text field. 
	 * 
	 * @param pickerChoice value of the picker to select.
	 * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
	 * @param timeout timeout (in seconds) for picker-related actions.
	 * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
	 */
	public void selectOption(String pickerChoice, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {
		List<String> pickerChoices = new ArrayList<String>();
		pickerChoices.add(pickerChoice);
		selectOption(pickerChoices, expectedFieldValue, timeout);
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
		MobileBuiltInKeywords.tap(testObject, timeout);

		// Sometimes there will be multiple pickers side-by-side, e.g. multipart date formats for month, date, year
		for (int i=0; i < pickerChoices.size(); i++) {
			selectOptionFromPicker(i, pickerChoices.get(i), timeout);
		}
		
		// Find the SELECT, DONE, or OK button on the picker wheel and tap it, applying the value to the field
		tapButtonWithText(Arrays.asList("OK", "SELECT", "Select", "DONE", "Done"));
		
		// It is possible to try to set the text of a field to something not in the picker list, in which case it fails silently.
		// We need to verify that the selection was made correctly, or throw an exception.
		
		String newTextFieldValue = getPickerValue(timeout);
		Logger.debug("Verifying that the picker value(s) of: " + pickerChoices + " was set correctly on " + testObject + " which now has a value of: " + newTextFieldValue);
		Logger.debug("Picker value set successfully!");
		if (!expectedFieldValue.equalsIgnoreCase(newTextFieldValue)) {
			throw(new NoSuchPickerChoiceException(pickerChoices));
		}
		
	}
}