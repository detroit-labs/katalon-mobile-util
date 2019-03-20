package com.detroitlabs.katalonmobileutil.component.mobile.android;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileComponentFactory;

/**
 * AndroidComponentFactory
 */
public class AndroidComponentFactory extends MobileComponentFactory {

    private static final String DEFAULT_REPOSITORY = "Android Objects";

    public AndroidComponentFactory() {
        this(DEFAULT_REPOSITORY);
    }

    public AndroidComponentFactory(String repository) {
        super(repository);
    }

    @Override
    public AndroidTextField createTextField(String name) {
        return new AndroidTextField(finder.findTextField(name));
    }
   
}