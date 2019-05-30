package com.detroitlabs.katalonmobileutil.component.mobile;

import com.detroitlabs.katalonmobileutil.component.ComponentFactory;
import com.detroitlabs.katalonmobileutil.component.Finder;

/**
 * MobileComponentFactory
 */
public abstract class MobileComponentFactory implements ComponentFactory {

    protected final Finder finder;

    public MobileComponentFactory(String repository) {
        finder = new Finder(repository);
    }

    @Override
    public abstract MobileTextField createTextField(String name);

    @Override
    public MobileComponent createAlert(String name) {
        return new MobileComponent(finder.findAlert(name));
    }

    @Override
    public MobilePressableComponent createButton(String name) {
        return new MobilePressableComponent(finder.findButton(name));
    }

    @Override
    public MobilePressableComponent createCheckbox(String name) {
        return new MobilePressableComponent(finder.findCheckbox(name));
    }

    @Override
    public MobileComponent createContainer(String name) {
        return new MobileComponent(finder.findContainer(name));
    }

    @Override
    public MobileComponent createImage(String name) {
        return new MobileComponent(finder.findImage(name));
    }

    @Override
    public MobileComponent createLabel(String name) {
        return new MobileComponent(finder.findLabel(name));
    }

    @Override
    public MobilePressableComponent createLink(String name) {
        return new MobilePressableComponent(finder.findLink(name));
    }

    @Override
    public MobileComponent createSegmentedControl(String name) {
        return new MobileComponent(finder.findSegmentedControl(name));
    }

    @Override
    public MobileTwoStatePressableComponent createSwitch(String name, String altStateName) {
        return new MobileTwoStatePressableComponent(finder.findSwitch(name), finder.findSwitch(altStateName));
    }

    @Override
    public MobilePressableComponent createTab(String name) {
        return new MobilePressableComponent(finder.findTab(name));
    }

    @Override
    public MobileComponent createGeneric(String name) {
        return new MobileComponent(finder.findGeneric(name));
    }
}