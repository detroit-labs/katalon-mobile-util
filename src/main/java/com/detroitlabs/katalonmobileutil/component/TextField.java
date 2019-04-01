package com.detroitlabs.katalonmobileutil.component;

/**
 * TextField
 */
public interface TextField {

    public void typeText(String text, Integer timeout);
    public void clearText();
    public void hideKeyboard();

    default public void typeText(String text) {
        typeText(text, Component.defaultTimeout);
    }
}