package com.detroitlabs.katalonmobileutil.component.mobile.ios;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileTextField;
import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.testobject.XPathBuilder;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.util.KeywordUtil;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.HideKeyboardStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * IOSTextField
 */
public class IOSTextField extends IOSComponent implements MobileTextField {

    public IOSTextField(TestObject textField) {
        super(textField);
    }

    public enum IOSReturnKey {
        RETURN("return"),
        GO("Go"),
        GOOGLE("Google"),
        JOIN("Join"),
        NEXT("Next"),
        ROUTE("Route"),
        SEARCH("Search"),
        SEND("Send"),
        YAHOO("Yahoo"),
        DONE("Done"),
        EMERGENCY_CALL("Emergency Call"),
        CONTINUE("Continue");

        private String name;

        IOSReturnKey(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    /**
     * Clear exising text in the TextField
     * @param timeout Seconds to wait before throwing an error.
     * @param clearButton Unused in iOS. Pass null to parameter.
     */
    @Override
    public void clearText(Integer timeout, TestObject clearButton) {
        // iOS identifies the text fields directly with accessibilityIds.
        // The iOS text field can be cleared directly.
        MobileBuiltInKeywords.clearText(testObject, timeout);
    }

    /**
     * Type text into the TextField
     * @param text String to set in the field.
     * @param timeout seconds to wait before throwing an error.
     */
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
    /**
     * Hide the keyboard using a variety of methods. Will continue if it is unsuccessful.
     * Starts with trying to use the Done button on the keyboard toolbar.
     * Then attempts all of the possible return key values in iOS (https://developer.apple.com/documentation/uikit/uireturnkeytype)
     * Then attempts to tap outside of the keyboard.
     */
    public void hideKeyboard() {
        IOSDriver<?> driver = (IOSDriver<?>)MobileDriverFactory.getDriver();
        if (isKeyboardShowing()) {
            // The hideKeyboard function often crashes in iOS.
            // Find the DONE button on the keyboard toolbar and tap it, closing the keyboard
            Logger.debug("Trying to close the keyboard by tapping the Done button in the keyboard toolbar.");
            tapKeyboardToolbarButtonWithText(Arrays.asList("DONE", "Done"));
            MobileBuiltInKeywords.delay(1);
            Logger.debug("driver keyboard: " + isKeyboardShowing());

            // If the Done button didn't work, try to close the keyboard using one of the predefined return keys
            // supported by iOS keyboards.
            if (isKeyboardShowing()) {
                for (IOSTextField.IOSReturnKey returnKey : IOSTextField.IOSReturnKey.values()) {
                    submitForm(returnKey);
                    if (!isKeyboardShowing()) break;
                }
            }

            // If the keyboard buttons didn't work, try tapping outside the keyboard
            if (isKeyboardShowing()) {
                Logger.debug("Trying to close the keyboard by tapping outside.");
                driver.hideKeyboard(HideKeyboardStrategy.TAP_OUTSIDE);
            }

            if (isKeyboardShowing()) {
                Logger.warn("Could not find a way to close the keyboard.");
                KeywordUtil.markWarning("Could not find a way to close the keyboard.");
            } else {
                Logger.debug("Keyboard successfully hidden.");
            }
        } else {
            Logger.debug("Keyboard is already hidden.");
        }
    }

    public void tapKeyboardToolbarButtonWithText(List<String> names) {
        String xpathForButton;
        if (Device.isIOS()) {
            String xpathForKeyboardToolbar = XPathBuilder.createXPath("XCUIElementTypeToolbar");
            String xpathForButtonOnly = XPathBuilder.xpathWithPossibleListOfValues("XCUIElementTypeButton", "label", names);
            xpathForButton = XPathBuilder.addChildXPath(xpathForKeyboardToolbar, xpathForButtonOnly);
        } else {
            xpathForButton = XPathBuilder.xpathWithPossibleListOfValues("android.widget.Button", "text", names);
        }

        TestObject button = new TestObject();
        button.addProperty("xpath", ConditionType.EQUALS, xpathForButton);
        Logger.debug("Tapping the button to close the keyboard or picker:");
        Logger.printTestObject(button, Logger.LogLevel.DEBUG);
        MobileBuiltInKeywords.tap(button, 1, FailureHandling.OPTIONAL);
    }

    /**
     * Submits the form given a specific iOS keyboard key.
     * Note: This is largely influenced by how the keyboard in the application is implemented; it might do nothing.
     * @param key - one of the predefined key iOS return key values
     */
    public void submitForm(IOSReturnKey key) {
        IOSDriver<?> driver = (IOSDriver<?>)MobileDriverFactory.getDriver();
        Logger.debug("Trying to close the keyboard with: " + key.getName());
        driver.hideKeyboard(HideKeyboardStrategy.PRESS_KEY, key.getName());
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

    @Override
    public boolean isKeyboardShowing() {
        IOSDriver<?> driver = (IOSDriver<?>)MobileDriverFactory.getDriver();
        try {
            driver.findElementByClassName("XCUIElementTypeKeyboard");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}