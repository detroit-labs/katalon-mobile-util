package com.detroitlabs.katalonmobileutil.component;

import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;

/**
 * Component
 */
public abstract class Component {
    public static final FailureHandling defaultFailureHandling = FailureHandling.STOP_ON_FAILURE;
    public static final Integer defaultTimeout = 0;

    public final TestObject testObject;

    protected Component(TestObject component) {
        this.testObject = component;
    }

    public abstract Boolean verifyElementPresent();
    public abstract Boolean verifyElementPresent(Integer timeoutInSeconds);
}