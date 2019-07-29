package com.detroitlabs.katalonmobileutil.component;

import com.kms.katalon.core.testobject.TestObject;

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject;

public class Finder {
    private final String repository;

    public Finder(String repository) {
        this.repository = repository;
    }

    public TestObject findAlert(String name) {
        return findObject("Alerts", name);
    }

    public TestObject findButton(String name) {
        return findObject("Buttons", name);
    }

    public TestObject findCheckbox(String name) {
        return findObject("Checkboxes", name);
    }

    public TestObject findContainer(String name) {
        return findObject("Containers", name);
    }

    public TestObject findImage(String name) {
        return findObject("Images", name);
    }

    public TestObject findLabel(String name) {
        return findObject("Labels", name);
    }

    public TestObject findLink(String name) {
        return findObject("Links", name);
    }

    public TestObject findSegmentedControl(String name) {
        return findObject("Segmented Controls", name);
    }

    public TestObject findSwitch(String name) {
        return findObject("Switches", name);
    }

    public TestObject findTab(String name) {
        return findObject("Tabs", name);
    }

    public TestObject findTextField(String name) {
        return findObject("Text Fields", name);
    }

    public TestObject findGeneric(String name) {
        return findObject(name);
    }

    private TestObject findObject(String name) {
        return findObject(null, name);
    }

    private TestObject findObject(String type, String name) {

        String root = repository != null ? repository + "/" : "";
        type = type != null ? type + "/" : "";

        String object = root + type + name;

        return findTestObject(object);
    }
}