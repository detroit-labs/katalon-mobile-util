package com.detroitlabs.katalonmobileutil.testobject;

public enum TestObjectType {
    ALERT("Alerts"),
    BUTTON("Buttons"),
    CHECKBOX("Checkboxes"),
    CONTAINER("Containers"),
    IMAGE("Images"),
    LABEL("Labels"),
    LINK("Link"),
    SEGMENTED_CONTROL("Segmented Controls"),
    SWITCH("Switches"),
    TAB("Tabs"),
    TEXT_FIELD("Text Fields"),
    GENERIC(null);

    private String name;

    TestObjectType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}