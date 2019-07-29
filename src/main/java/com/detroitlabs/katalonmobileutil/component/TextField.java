package com.detroitlabs.katalonmobileutil.component;

/**
 * TextField
 */
public interface TextField extends Component {

    void typeText(String text, Integer timeout);

    void clearText();

    default void typeText(String text) {
        typeText(text, defaultTimeout);
    }
}