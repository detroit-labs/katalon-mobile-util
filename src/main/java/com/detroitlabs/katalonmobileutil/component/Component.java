package com.detroitlabs.katalonmobileutil.component;

import com.detroitlabs.katalonmobileutil.device.Platform;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.model.FailureHandling;

/**
 * Component
 */
public interface Component {
    /**
     * The default error handling from Katalon's RunConfiguration.
     */
    FailureHandling defaultFailureHandling = RunConfiguration.getDefaultFailureHandling();
    /**
     * The timeout from Katalon's RunConfiguration.
     */
    Integer defaultTimeout = RunConfiguration.getTimeOut();

    /**
     * Check if the element is in the UI.
     * @param timeoutInSeconds The amount of time to wait before failing this step.
     * @return True if the element is in the UI.
     */
    Boolean verifyElementPresent(Integer timeoutInSeconds);

    /**
     * Check if the element is in the UI. Uses the default timeout.
     * @return True if the element is in the UI.
     */
    default Boolean verifyElementPresent() {
        return verifyElementPresent(defaultTimeout);
    }

    /**
     * Get the Platform the Component is for.
     * @return The Platform the Component is for.
     */
    Platform getPlatform();

    /**
     * Check if the Component is for Android.
     * @return True if the Component is for Android.
     */
    default Boolean isAndroid() {
        return Platform.ANDROID.equals(getPlatform());
    }

    /**
     * Check if the Component is for iOS.
     * @return True if the Component is for iOS.
     */
    default Boolean isIOS() {
        return Platform.IOS.equals(getPlatform());
    }

    /**
     * Check if the Component is for all mobile platforms.
     * @return True if the Component is for all mobile platforms.
     */
    default Boolean isMobile() {
        return Platform.MOBILE.equals(getPlatform());
    }

    /**
     * Check if the Component is for web.
     * @return True if the Component is for web.
     */
    default Boolean isWeb() {
        return Platform.WEB.equals(getPlatform());
    }
}