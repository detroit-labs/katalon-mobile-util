package com.detroitlabs.katalonmobileutil.component.mobile.ios;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileTextField;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import io.appium.java_client.AppiumDriver;

import java.util.Arrays;
import java.util.List;

/**
 * IOSTextField
 */
public class IOSTextField extends IOSComponent implements MobileTextField {

    public IOSTextField(TestObject textField) {
        super(textField);
    }

    @Override
    public void clearText(Integer timeout, TestObject clearButton) {
        // iOS identifies the text fields directly with accessibilityIds.
        // The iOS text field can be cleared directly.
        MobileBuiltInKeywords.clearText(testObject, timeout);
    }

    @Override
    public void typeText(String text, Integer timeout) {
        // iOS identifies the text fields directly with accessibilityIds.
        // Text can be set directly on the iOS field.
        MobileBuiltInKeywords.setText(testObject, text, timeout);
    }

    @Override
    public void selectOptionFromPicker(int pickerIndex, String pickerChoice, int timeout) {
        // Find the picker wheel and set its text, which spins the wheel
        TestObject pickerWheel = new TestObject();
        pickerWheel.addProperty("xpath", ConditionType.EQUALS, "(//XCUIElementTypePickerWheel)[" + (pickerIndex + 1) + "]");
        MobileBuiltInKeywords.sendKeys(pickerWheel, pickerChoice);
        Logger.debug("Current pickerWheel value: " + MobileBuiltInKeywords.getText(pickerWheel, timeout));
    }

    @Override
    public String getPickerValue(Integer timeout) {
        return MobileBuiltInKeywords.getText(testObject, timeout);
    }

    @Override
    public void nextField() {
        // For iOS, look at the buttons and find one that matches "Next", but there can be a few variations
        tapButtonWithText(Arrays.asList("NEXT", "Next", ">"));
    }

    @Override
    public void hideKeyboard() {
        AppiumDriver<?> driver = (AppiumDriver<?>) MobileDriverFactory.getDriver();
        if (driver.getKeyboard() != null) {
            // The hideKeyboard function often crashes in iOS.
            // Find the DONE button on the keyboard toolbar and tap it, closing the keyboard
            tapButtonWithText(Arrays.asList("DONE", "Done"));
        } else {
            Logger.debug("Keyboard is already hidden.");
        }
    }

    @Override
    public void tapButtonWithText(List<String> names) {
        tapButtonWithText(names, "XCUIElementTypeButton", "name");
    }

    /**
     * Select multiple values from a multipart picker.
     *
     * @param pickerChoices      value of the option to select for each part of the picker.
     * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
     * @param timeout            timeout (in seconds) for picker-related actions.
     * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
     */
    public void selectOption(List<String> pickerChoices, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {

        // Open the picker by tapping on the field that triggers it
        MobileBuiltInKeywords.tap(testObject, timeout);

        // Sometimes there will be multiple pickers side-by-side, e.g. multipart date formats for month, date, year
        for (int i = 0; i < pickerChoices.size(); i++) {
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
            throw (new NoSuchPickerChoiceException(pickerChoices));
        }

    }
}