package com.detroitlabs.katalonmobileutil.device;

public enum Platform {
    ANDROID("Android"),
    IOS("iOS"),
    MOBILE("Mobile"),
    WEB("Web");

    private String name;

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
