package com.detroitlabs.katalonmobileutil.component;

import com.kms.katalon.core.model.FailureHandling;

/**
 * A PressableComponent with a second TestObject.
 */
public interface TwoStatePressableComponent extends PressableComponent {
    
    /**
     * Press the alternate TestObject.
     * 
     * @param timeoutInSeconds The maximum time in seconds to attempt to find the element.
     * @param failureHandling The handling if the Component cannot be pressed.
     */
    public void pressAlternate(int timeoutInSeconds, FailureHandling failureHandling);

    /**
     * Press the alternate TestObject. Uses the default timeout and failure handling from the Component class.
     */
    default public void pressAlternate() {
        press(Component.defaultTimeout, Component.defaultFailureHandling);
    }

    /**
     * Press the alternate TestObject. Uses the default failure handling from the Component class.
     * 
     * @param timeoutInSeconds The maximum time in seconds to attempt to find the element.
     */
    default public void pressAlternate(int timeoutInSeconds) {
        press(timeoutInSeconds, Component.defaultFailureHandling);
    }

    /**
     * Press the alternate TestObject. Uses the default timeout from the Component class.
     * 
     * @param failureHandling The handling if the Component cannot be pressed.
     */
    default public void pressAlternate(FailureHandling failureHandling) {
        press(Component.defaultTimeout, failureHandling);
    }
}