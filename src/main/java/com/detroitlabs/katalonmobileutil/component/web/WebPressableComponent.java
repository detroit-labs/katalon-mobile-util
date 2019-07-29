package com.detroitlabs.katalonmobileutil.component.web;

import com.detroitlabs.katalonmobileutil.component.PressableComponent;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

/**
 * WebPressableComponent
 */
public class WebPressableComponent extends WebComponent implements PressableComponent {

    public WebPressableComponent(TestObject component) {
        super(component);
    }

    @Override
    public void press(int timeoutInSeconds, FailureHandling failureHandling) {
        WebUiBuiltInKeywords.click(testObject, failureHandling);
    }
}