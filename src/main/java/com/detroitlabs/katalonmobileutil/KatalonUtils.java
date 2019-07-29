package com.detroitlabs.katalonmobileutil;

import com.detroitlabs.katalonmobileutil.component.*;
import com.detroitlabs.katalonmobileutil.component.mobile.android.AndroidComponentFactory;
import com.detroitlabs.katalonmobileutil.component.mobile.ios.IOSComponentFactory;
import com.detroitlabs.katalonmobileutil.component.web.WebComponentFactory;
import com.detroitlabs.katalonmobileutil.device.Device;

public class KatalonUtils {

    private static ComponentFactory componentFactory = createComponentFactory();

    /**
     * Create a ComponentFactory instance for the correct platform
     * based on the currently loaded driver in Katalon Studio.
     *
     * @return A ComponentFactory for the platform being tested.
     */
    private static ComponentFactory createComponentFactory() {
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
    public static TextField createTextField(String name) {
        return componentFactory.createTextField(name);
    }

    /**
     * Get an Alert Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return An Alert Component from Object Repository/(Web/IOS/Android) Objects/Alerts.
     */
    public static Component createAlert(String name) {
        return componentFactory.createAlert(name);
    }

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Buttons.
     */
    public static PressableComponent createButton(String name) {
        return componentFactory.createButton(name);
    }

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Checkboxes.
     */
    public static PressableComponent createCheckbox(String name) {
        return componentFactory.createCheckbox(name);
    }

    /**
     * Get a Container Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Container Component from Object Repository/(Web/IOS/Android) Objects/Containers.
     */
    public static Component createContainer(String name) {
        return componentFactory.createContainer(name);
    }

    /**
     * Get an Image Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return An Image Component from Object Repository/(Web/IOS/Android) Objects/Images.
     */
    public static Component createImage(String name) {
        return componentFactory.createImage(name);
    }

    /**
     * Get a Label Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Label Component from Object Repository/(Web/IOS/Android) Objects/Labels.
     */
    public static Component createLabel(String name) {
        return componentFactory.createLabel(name);
    }

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Links.
     */
    public static PressableComponent createLink(String name) {
        return componentFactory.createLink(name);
    }

    /**
     * Get a SegmentedControl Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A SegmentedControl Component from Object Repository/(Web/IOS/Android) Objects/Segmented Controls.
     */
    public static Component createSegmentedControl(String name) {
        return componentFactory.createSegmentedControl(name);
    }

    /**
     * Get a TwoStatePressableComponent by name.
     *
     * @param name         The TestObject's name in the Object Repository.
     * @param altStateName The secondary TestObject's name in the Object Repository.
     * @return A TwoStatePressableComponent from Object Repository/(Web/IOS/Android) Objects/Switches.
     */
    public static TwoStatePressableComponent createSwitch(String name, String altStateName) {
        return componentFactory.createSwitch(name, altStateName);
    }

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Tabs.
     */
    public static PressableComponent createTab(String name) {
        return componentFactory.createTab(name);
    }

    /**
     * Get a Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Component from Object Repository/(Web/IOS/Android) Objects.
     */
    public static Component createGeneric(String name) {
        return componentFactory.createGeneric(name);
    }
}
