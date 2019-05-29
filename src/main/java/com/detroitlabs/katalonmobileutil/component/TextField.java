package com.detroitlabs.katalonmobileutil.component;

/**
 * TextField
 */
public interface TextField {

    void typeText(String text, Integer timeout);

    void clearText();

    default void typeText(String text) {
        typeText(text, Component.defaultTimeout);
    }
}