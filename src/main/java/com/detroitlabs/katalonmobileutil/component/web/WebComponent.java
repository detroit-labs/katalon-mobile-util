package com.detroitlabs.katalonmobileutil.component.web;

import com.detroitlabs.katalonmobileutil.component.Component;
import com.detroitlabs.katalonmobileutil.device.Platform;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

/**
 * WebComponent
 */
public class WebComponent implements Component {

    public final TestObject testObject;

    WebComponent(TestObject testObject) {
        this.testObject = testObject;
    }

    @Override
    public Boolean verifyElementPresent() {
        return verifyElementPresent(defaultTimeout);
    }

    @Override
    public Boolean verifyElementPresent(Integer timeoutInSeconds) {
        return WebUiBuiltInKeywords.verifyElementPresent(testObject, timeoutInSeconds);
    }

    @Override
    public Platform getPlatform() {
        return Platform.WEB;
    }
}