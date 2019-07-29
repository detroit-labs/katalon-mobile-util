package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.Component;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.testobject.TestObject;

/**
 * MobileComponent
 */
class MobileComponent extends Component {

    MobileComponent(TestObject component) {
        super(component);
    }

    @Override
    public Boolean verifyElementPresent() {
        return verifyElementPresent(defaultTimeout);
    }

    @Override
    public Boolean verifyElementPresent(Integer timeoutInSeconds) {
        return MobileBuiltInKeywords.verifyElementExist(testObject, timeoutInSeconds);
    }
}