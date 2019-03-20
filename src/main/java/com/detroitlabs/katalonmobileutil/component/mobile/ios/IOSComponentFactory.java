package com.detroitlabs.katalonmobileutil.component.mobile.ios;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileComponentFactory;

/**
 * IOSComponentFactory
 */
public class IOSComponentFactory extends MobileComponentFactory {

    private static final String DEFAULT_REPOSITORY = "IOS Objects";

    public IOSComponentFactory() {
        this(DEFAULT_REPOSITORY);
    }

    public IOSComponentFactory(String repository) {
        super(repository);
    }

    @Override
    public IOSTextField createTextField(String name) {
        return new IOSTextField(finder.findTextField(name));
    }
}