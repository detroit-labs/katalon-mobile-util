package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.PressableComponent;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;

/**
 * MobilePressableComponent
 */
public class MobilePressableComponent extends MobileComponent implements PressableComponent {

    public MobilePressableComponent(TestObject component) {
        super(component);
    }

    public void press(int timeoutInSeconds, FailureHandling failureHandling) {
        MobileBuiltInKeywords.tap(testObject, timeoutInSeconds, failureHandling);
    }
}