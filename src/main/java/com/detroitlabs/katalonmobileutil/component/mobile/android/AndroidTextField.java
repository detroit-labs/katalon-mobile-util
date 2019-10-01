package com.detroitlabs.katalonmobileutil.component.mobile.android;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileTextField;
import com.detroitlabs.katalonmobileutil.exception.NoSuchPickerChoiceException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.testobject.Finder;
import com.detroitlabs.katalonmobileutil.testobject.TestObjectConverter;
import com.detroitlabs.katalonmobileutil.testobject.XPathBuilder;
import com.detroitlabs.katalonmobileutil.touch.Scroll;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.NoSuchElementException;

import java.util.List;

/**
 * AndroidTextField
 */
public class AndroidTextField extends AndroidComponent implements MobileTextField {

    public AndroidTextField(TestObject textField) {
        super(textField);
    }

    @Override
    public void clearText(Integer timeout, TestObject clearButton) {
        // Android wraps the text fields in RelativeLayouts, which in turn get the resource-id.

        // First tap the field to activate it
        MobileBuiltInKeywords.tap(testObject, timeout);

        // Press the clear button, if it was provided
        if (clearButton != null) {
            MobileBuiltInKeywords.tap(clearButton, timeout, FailureHandling.OPTIONAL);
        }
    }

    @Override
    public void typeText(String text, Integer timeout) {
        // Android wraps the text fields in RelativeLayouts, which in turn get the resource-id.
        // However, we can't set the text directly on the RelativeLayout, nor can we get to the EditText
        // field within it.

        // First tap the field to activate it, bringing up the keyboard
        MobileBuiltInKeywords.tap(testObject, timeout);

        typeTextOnKeyboard(text);
    }

    @Override
    public void selectOptionFromPicker(int pickerIndex, String pickerChoice, int timeout) {
        try {
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
        } catch (NoSuchElementException e) {
            selectOptionFromTextList(pickerChoice, timeout);
        }
    }

    @Override
    public String getPickerValue(Integer timeout) {
        String type = TestObjectConverter.typeFromTestObject(testObject);

        String xpath = XPathBuilder.createXPath(type);

        String resourceId = TestObjectConverter.resourceIdFromTestObject(testObject);
        xpath = XPathBuilder.addResourceId(xpath, resourceId);

        // Android spinners don't expose their text - we have to dig in to get the TextView 
        if (type.contains("Spinner")) {
            xpath = XPathBuilder.addChildWithType(xpath, "TextView");
        }

        TestObject spinner = new TestObject();
        spinner.addProperty("xpath", ConditionType.EQUALS, xpath);

        return MobileBuiltInKeywords.getText(spinner, timeout);
    }

    @Override
    public void nextField() {
        // For Android, we can use the Tab key
        AndroidDriver<?> driver = (AndroidDriver<?>) MobileDriverFactory.getDriver();
        driver.pressKey(new KeyEvent(AndroidKey.TAB));
    }

    @Override
    public void hideKeyboard() {
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        if (driver.getKeyboard() != null) {
            // Android's hideKeyboard seems more reliable.
            driver.hideKeyboard();
        } else {
            Logger.debug("Keyboard is already hidden.");
        }
    }

    @Override
    public void tapButtonWithText(List<String> names) {
        tapButtonWithText(names, "android.widget.Button", "text");
    }

    private void selectOptionFromTextList(String pickerChoice, Integer timeout) {
        // When the "picker" is just a list of labels
        Scroll.scrollListToElementWithText(pickerChoice, timeout);
        TestObject selection = Finder.findLabelWithText(pickerChoice);
        MobileBuiltInKeywords.tap(selection, timeout);
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
        AndroidDriver<?> driver = (AndroidDriver<?>)MobileDriverFactory.getDriver();
        return driver.isKeyboardShown();
    }

}