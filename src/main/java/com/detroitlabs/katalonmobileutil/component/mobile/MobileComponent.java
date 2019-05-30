package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.Component;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.testobject.TestObject;

/**
 * MobileComponent
 */
public class MobileComponent implements Component {

    public final TestObject testObject;

    protected MobileComponent(TestObject testObject) {
        this.testObject = testObject;
    }

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