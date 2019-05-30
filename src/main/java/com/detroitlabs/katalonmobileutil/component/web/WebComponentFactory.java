package com.detroitlabs.katalonmobileutil.component.web;

import com.detroitlabs.katalonmobileutil.component.ComponentFactory;
import com.detroitlabs.katalonmobileutil.component.Finder;

/**
 * WebComponentFactory
 */
public class WebComponentFactory implements ComponentFactory {

    private static final String DEFAULT_REPOSITORY = "Web Objects";

    /**
     * The finder to use for getting TestObjects from the Object Repository.
     */
    protected final Finder finder;

    public WebComponentFactory() {
        this(DEFAULT_REPOSITORY);
    }

    public WebComponentFactory(String repository) {
        finder = new Finder(repository);
    }

    @Override
    public WebTextField createTextField(String name) {
        return new WebTextField(finder.findTextField(name));
    }

    @Override
    public WebAlert createAlert(String name) {
        return new WebAlert(finder.findAlert(name));
    }

    @Override
    public WebPressableComponent createButton(String name) {
        return new WebPressableComponent(finder.findButton(name));
    }

    @Override
    public WebPressableComponent createCheckbox(String name) {
        return new WebPressableComponent(finder.findCheckbox(name));
    }

    @Override
    public WebComponent createContainer(String name) {
        return new WebComponent(finder.findContainer(name));
    }

    @Override
    public WebComponent createImage(String name) {
        return new WebComponent(finder.findImage(name));
    }

    @Override
    public WebComponent createLabel(String name) {
        return new WebComponent(finder.findLabel(name));
    }

    @Override
    public WebPressableComponent createLink(String name) {
        return new WebPressableComponent(finder.findLink(name));
    }

    @Override
    public WebComponent createSegmentedControl(String name) {
        return new WebComponent(finder.findSegmentedControl(name));
    }

    @Override
    public WebTwoStatePressableComponent createSwitch(String name, String altStateName) {
        return new WebTwoStatePressableComponent(finder.findSwitch(name), finder.findSwitch(altStateName));
    }

    @Override
    public WebPressableComponent createTab(String name) {
        return new WebPressableComponent(finder.findTab(name));
    }

    @Override
    public WebComponent createGeneric(String name) {
        return new WebComponent(finder.findGeneric(name));
    }
}