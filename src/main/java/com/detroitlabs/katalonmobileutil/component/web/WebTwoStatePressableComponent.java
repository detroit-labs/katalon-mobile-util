package com.detroitlabs.katalonmobileutil.component.web;

import com.detroitlabs.katalonmobileutil.component.TwoStatePressableComponent;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

/**
 * WebSwitch
 */
public class WebTwoStatePressableComponent extends WebPressableComponent implements TwoStatePressableComponent {
    protected TestObject altTestObject;

	public WebTwoStatePressableComponent(TestObject toSwitch, TestObject altState) {
        super(toSwitch);
        altTestObject = altState;
    }
    
    public void pressAlternate(int timeoutInSeconds, FailureHandling failureHandling) {
        WebUiBuiltInKeywords.click(testObject, failureHandling);
    }
}