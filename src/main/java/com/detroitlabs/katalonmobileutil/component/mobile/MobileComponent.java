package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.Component;
import com.detroitlabs.katalonmobileutil.device.Platform;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.ConditionType;
import com.kms.katalon.core.testobject.TestObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.interactions.Keyboard;

import java.util.List;

/**
 * MobileComponent
 */
public class MobileComponent implements Component {

    public final TestObject testObject;

    protected MobileComponent(TestObject testObject) {
        this.testObject = testObject;
    }

    public static void tapButtonWithText(List<String> names, String objectClass, String objectProperty) {
        TestObject button = new TestObject();
        // Xpath 1 (used by Selenium) doesn't have matches, so this is a substitute
        StringBuilder sb = new StringBuilder();
        sb.append("@").append(objectProperty).append("='").append(names.get(0)).append("'");
        for (int i = 1; i < names.size(); i++) {
            sb.append(" or @").append(objectProperty).append("='").append(names.get(i)).append("'");
        }
        button.addProperty("xpath", ConditionType.EQUALS, "//" + objectClass + "[" + sb.toString() + "]");
        Logger.debug("Tapping the button to close the keyboard or picker:");
        Logger.printTestObject(button, Logger.LogLevel.DEBUG);
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
    public Boolean verifyElementPresent() {
        return verifyElementPresent(defaultTimeout);
    }

    @Override
    public Boolean verifyElementPresent(Integer timeoutInSeconds) {
        return MobileBuiltInKeywords.verifyElementExist(testObject, timeoutInSeconds);
    }

    @Override
    public Platform getPlatform() {
        return Platform.MOBILE;
    }
}