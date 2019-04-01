package com.detroitlabs.katalonmobileutil.component.web;

import com.detroitlabs.katalonmobileutil.component.TextField;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;

/**
 * WebTextField
 */
public class WebTextField extends WebComponent implements TextField {

    public WebTextField(TestObject textField) {
        super(textField);
	}

    @Override
	public void typeText(String text, Integer timeout) {
        WebUiBuiltInKeywords.sendKeys(testObject, text);
    }

    @Override
    public void clearText() {
        clearText(defaultFailureHandling);
    }

    public void clearText(FailureHandling failureHandling) {
        WebUiBuiltInKeywords.clearText(testObject, failureHandling);
    }

    @Override
    public void hideKeyboard() {
        // No need to do anything in web.
    }

    
}