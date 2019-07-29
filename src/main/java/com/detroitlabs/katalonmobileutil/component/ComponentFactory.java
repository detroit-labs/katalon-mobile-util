package com.detroitlabs.katalonmobileutil.component;

import com.detroitlabs.katalonmobileutil.component.mobile.android.AndroidComponentFactory;
import com.detroitlabs.katalonmobileutil.component.mobile.ios.IOSComponentFactory;
import com.detroitlabs.katalonmobileutil.component.web.WebComponentFactory;
import com.detroitlabs.katalonmobileutil.device.Device;

/**
 * Creates Components based on the target platform.
 */
public interface ComponentFactory {

    /**
     * Create a ComponentFactory instance for the correct platform
     * based on the currently loaded driver in Katalon Studio.
     *
     * @return A ComponentFactory for the platform being tested.
     */
    @Deprecated
    static ComponentFactory getComponentFactory() {
        // TODO: Remove this because it create a cyclic dependency on the implementation classes. Breaking change.
        if (Device.isAndroid()) {
            return new AndroidComponentFactory();
        }
        else if (Device.isIOS()) {
            return new IOSComponentFactory();
        }
        else {
            return new WebComponentFactory();
        }
    }

    /**
     * Get a TextField Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A TextField Component from Object Repository/(Web/IOS/Android) Objects/Text Fields
     */
    TextField createTextField(String name);

    /**
     * Get an Alert Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return An Alert Component from Object Repository/(Web/IOS/Android) Objects/Alerts.
     */
    Component createAlert(String name);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Buttons.
     */
    PressableComponent createButton(String name);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Checkboxes.
     */
    PressableComponent createCheckbox(String name);

    /**
     * Get a Container Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Container Component from Object Repository/(Web/IOS/Android) Objects/Containers.
     */
    Component createContainer(String name);

    /**
     * Get an Image Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return An Image Component from Object Repository/(Web/IOS/Android) Objects/Images.
     */
    Component createImage(String name);

    /**
     * Get a Label Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Label Component from Object Repository/(Web/IOS/Android) Objects/Labels.
     */
    Component createLabel(String name);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Links.
     */
    PressableComponent createLink(String name);

    /**
     * Get a SegmentedControl Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A SegmentedControl Component from Object Repository/(Web/IOS/Android) Objects/Segmented Controls.
     */
    Component createSegmentedControl(String name);

    /**
     * Get a TwoStatePressableComponent by name.
     *
     * @param name         The TestObject's name in the Object Repository.
     * @param altStateName The secondary TestObject's name in the Object Repository.
     * @return A TwoStatePressableComponent from Object Repository/(Web/IOS/Android) Objects/Switches.
     */
    TwoStatePressableComponent createSwitch(String name, String altStateName);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Tabs.
     */
    PressableComponent createTab(String name);

    /**
     * Get a Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Component from Object Repository/(Web/IOS/Android) Objects.
     */
    Component createGeneric(String name);

}