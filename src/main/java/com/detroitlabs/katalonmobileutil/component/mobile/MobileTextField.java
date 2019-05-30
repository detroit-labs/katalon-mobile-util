package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.TextField;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.interactions.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MobileTextField
 */
public interface MobileTextField extends TextField {

    /**
     * Moves the keyboard focus to the next field in the form
     */
    void nextField();

    void hideKeyboard();

    void selectOptionFromPicker(int pickerIndex, String pickerChoice, int timeout);

    void selectOption(List<String> pickerChoice, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException;

    String getPickerValue(Integer timeout);

    void tapButtonWithText(List<String> names);

    void clearText(Integer timeout, TestObject clearButton);

    @Override
    default void clearText() {
        clearText(defaultTimeout);
    }

    default void clearText(Integer timeout) {
        clearText(timeout, null);
    }

    /**
     * Type text at the current cursor position. Requires that a TextField is already active and the keyboard is visible.
     *
     * @param text text to type into the field.
     */
    @Override
    default void typeText(String text) {
        typeText(text, defaultTimeout);
    }

    /**
     * Choose a single value from a drop-down or picker.
     *
     * @param pickerChoice value of the picker to select.
     * @param timeout      timeout (in seconds) for picker-related actions.
     * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
     */
    default void selectOption(String pickerChoice, Integer timeout) throws NoSuchPickerChoiceException {
        List<String> pickerChoices = new ArrayList<String>();
        pickerChoices.add(pickerChoice);
        // Assume that the picker choice is also the string that will be used to validate the selection.
        selectOption(pickerChoices, pickerChoice, timeout);
    }

    /**
     * Choose a single value from a drop-down or picker. Use this in cases where the picker value is transformed before
     * showing in the text field.
     *
     * @param pickerChoice       value of the picker to select.
     * @param expectedFieldValue value used to make sure that the picker choices were made correctly.
     * @param timeout            timeout (in seconds) for picker-related actions.
     * @throws NoSuchPickerChoiceException when no matching picker options were found for the requested choice.
     */
    default void selectOption(String pickerChoice, String expectedFieldValue, Integer timeout) throws NoSuchPickerChoiceException {
        List<String> pickerChoices = new ArrayList<String>();
        pickerChoices.add(pickerChoice);
        selectOption(pickerChoices, expectedFieldValue, timeout);
    }
}