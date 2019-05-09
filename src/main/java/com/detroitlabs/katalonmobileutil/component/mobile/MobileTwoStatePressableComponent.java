package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.TwoStatePressableComponent;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;

/**
 * MobileTwoStatePressableComponent
 */
public class MobileTwoStatePressableComponent extends MobilePressableComponent implements TwoStatePressableComponent {
    protected TestObject altTestObject;

    public MobileTwoStatePressableComponent(TestObject toSwitch, TestObject altState) {
        super(toSwitch);
        altTestObject = altState;
    }

    @Override
    public void pressAlternate(int timeoutInSeconds, FailureHandling failureHandling) {
        MobileBuiltInKeywords.tap(altTestObject, timeoutInSeconds, failureHandling);
    }
}