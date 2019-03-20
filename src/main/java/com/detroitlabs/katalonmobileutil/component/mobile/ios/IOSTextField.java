package com.detroitlabs.katalonmobileutil.component.mobile.ios;

import java.util.Arrays;
import java.util.List;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileTextField;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;

import io.appium.java_client.AppiumDriver;

/**
 * IOSTextField
 */
public class IOSTextField extends MobileTextField {

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
}