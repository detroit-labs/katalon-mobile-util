package com.detroitlabs.katalonmobileutil.component;

import com.detroitlabs.katalonmobileutil.component.mobile.android.AndroidComponentFactory;
import com.detroitlabs.katalonmobileutil.component.mobile.ios.IOSComponentFactory;
import com.detroitlabs.katalonmobileutil.component.web.WebComponentFactory;
import com.detroitlabs.katalonmobileutil.device.Device;

/**
 * Creates Components based on the target platform.
 */
public abstract class ComponentFactory {

    /**
     * The finder to use for getting TestObjects from the Object Repository.
     */
    protected final Finder finder;

    protected ComponentFactory(String repository) {
        finder = new Finder(repository);
    }

    /**
     * Create a ComponentFactory instance for the correct platform
     * based on the currently loaded driver in Katalon Studio.
     *
     * @return A ComponentFactory for the platform being tested.
     */
    public static ComponentFactory getComponentFactory() {
        if (Device.isWeb()) {
            return new WebComponentFactory();
        } else if (Device.isIOS()) {
            return new IOSComponentFactory();
        } else {
            return new AndroidComponentFactory();
        }
    }

    /**
     * Get a TextField Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A TextField Component from Object Repository/(Web/IOS/Android) Objects/Text Fields
     */
    public abstract TextField createTextField(String name);

    /**
     * Get an Alert Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return An Alert Component from Object Repository/(Web/IOS/Android) Objects/Alerts.
     */
    public abstract Component createAlert(String name);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Buttons.
     */
    public abstract PressableComponent createButton(String name);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Checkboxes.
     */
    public abstract PressableComponent createCheckbox(String name);

    /**
     * Get a Container Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Container Component from Object Repository/(Web/IOS/Android) Objects/Containers.
     */
    public abstract Component createContainer(String name);

    /**
     * Get an Image Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return An Image Component from Object Repository/(Web/IOS/Android) Objects/Images.
     */
    public abstract Component createImage(String name);

    /**
     * Get a Label Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Label Component from Object Repository/(Web/IOS/Android) Objects/Labels.
     */
    public abstract Component createLabel(String name);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Links.
     */
    public abstract PressableComponent createLink(String name);

    /**
     * Get a SegmentedControl Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A SegmentedControl Component from Object Repository/(Web/IOS/Android) Objects/Segmented Controls.
     */
    public abstract Component createSegmentedControl(String name);

    /**
     * Get a TwoStatePressableComponent by name.
     *
     * @param name         The TestObject's name in the Object Repository.
     * @param altStateName The secondary TestObject's name in the Object Repository.
     * @return A TwoStatePressableComponent from Object Repository/(Web/IOS/Android) Objects/Switches.
     */
    public abstract TwoStatePressableComponent createSwitch(String name, String altStateName);

    /**
     * Get a PressableComponent by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A PressableComponent from Object Repository/(Web/IOS/Android) Objects/Tabs.
     */
    public abstract PressableComponent createTab(String name);

    /**
     * Get a Component by name.
     *
     * @param name The TestObject's name in the Object Repository.
     * @return A Component from Object Repository/(Web/IOS/Android) Objects.
     */
    public abstract Component createGeneric(String name);

}