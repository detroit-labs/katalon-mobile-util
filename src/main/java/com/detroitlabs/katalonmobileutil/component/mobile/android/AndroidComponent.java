package com.detroitlabs.katalonmobileutil.component.mobile.android;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileComponent;
import com.detroitlabs.katalonmobileutil.device.Platform;
import com.kms.katalon.core.testobject.TestObject;

public class AndroidComponent extends MobileComponent {

    public AndroidComponent(TestObject testObject) {
        super(testObject);
    }

    @Override
    public Platform getPlatform() {
        return Platform.ANDROID;
    }
}
