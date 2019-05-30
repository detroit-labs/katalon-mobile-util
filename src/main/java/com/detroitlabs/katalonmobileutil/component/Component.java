package com.detroitlabs.katalonmobileutil.component;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.model.FailureHandling;

/**
 * Component
 */
public interface Component {
    FailureHandling defaultFailureHandling = RunConfiguration.getDefaultFailureHandling();
    Integer defaultTimeout = RunConfiguration.getTimeOut();

    default Boolean verifyElementPresent() {
        return verifyElementPresent(defaultTimeout);
    }

    Boolean verifyElementPresent(Integer timeoutInSeconds);
}