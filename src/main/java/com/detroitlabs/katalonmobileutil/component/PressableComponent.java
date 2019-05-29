package com.detroitlabs.katalonmobileutil.component;

import com.kms.katalon.core.model.FailureHandling;

/**
 * A Component that can be pressed, clicked, or tapped.
 */
public interface PressableComponent {

    /**
     * Press the Component.
     *
     * @param timeoutInSeconds The maximum time in seconds to attempt to find the element.
     * @param failureHandling  The handling if the Component cannot be pressed.
     */
    void press(int timeoutInSeconds, FailureHandling failureHandling);

    /**
     * Press the Component. Uses the default timeout and failure handling from the Component class.
     */
    default void press() {
        press(Component.defaultTimeout, Component.defaultFailureHandling);
    }

    /**
     * Press the Component. Uses the default failure handling from the Component class.
     *
     * @param timeoutInSeconds The maximum time in seconds to attempt to find the element.
     */
    default void press(int timeoutInSeconds) {
        press(timeoutInSeconds, Component.defaultFailureHandling);
    }

    /**
     * Press the Component. Uses the default timeout from the Component class.
     *
     * @param failureHandling The handling if the Component cannot be pressed.
     */
    default void press(FailureHandling failureHandling) {
        press(Component.defaultTimeout, failureHandling);
    }

}