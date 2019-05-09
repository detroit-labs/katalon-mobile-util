package com.detroitlabs.katalonmobileutil.component.web;

import com.detroitlabs.katalonmobileutil.component.Component;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

/**
 * WebComponent
 */
class WebComponent extends Component {

    WebComponent(TestObject component) {
        super(component);
    }

    @Override
    public Boolean verifyElementPresent() {
        return verifyElementPresent(defaultTimeout);
    }

    @Override
    public Boolean verifyElementPresent(Integer timeoutInSeconds) {
        return WebUiBuiltInKeywords.verifyElementPresent(testObject, timeoutInSeconds);
    }
}