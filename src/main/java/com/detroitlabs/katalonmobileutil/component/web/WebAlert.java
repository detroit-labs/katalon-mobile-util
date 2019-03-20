package com.detroitlabs.katalonmobileutil.component.web;

import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

/**
 * WebAlert
 */
public class WebAlert extends WebComponent {

    public WebAlert(TestObject alert) {
        super(alert);
    }

    public void dismiss(int timeoutInSeconds, FailureHandling failureHandling) {
        WebUiBuiltInKeywords.dismissAlert(failureHandling);
    }

    public void dismiss() {
        dismiss(defaultTimeout, defaultFailureHandling);
    }
    
    public void dismiss(int timeoutInSeconds) {
        dismiss(timeoutInSeconds, defaultFailureHandling);
    }
    
    public void dismiss(FailureHandling failureHandling) {
        dismiss(defaultTimeout, failureHandling);
    }

    public void accept(int timeoutInSeconds, FailureHandling failureHandling) {
        WebUiBuiltInKeywords.acceptAlert(failureHandling);
    }

    public void accept() {
        accept(defaultTimeout, defaultFailureHandling);
    }
    
    public void accept(int timeoutInSeconds) {
        accept(timeoutInSeconds, defaultFailureHandling);
    }
    
    public void accept(FailureHandling failureHandling) {
        accept(defaultTimeout, failureHandling);
    }
}