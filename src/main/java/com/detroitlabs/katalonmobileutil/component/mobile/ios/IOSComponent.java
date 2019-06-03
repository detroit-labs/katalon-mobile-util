package com.detroitlabs.katalonmobileutil.component.mobile.ios;

import com.detroitlabs.katalonmobileutil.component.mobile.MobileComponent;
import com.detroitlabs.katalonmobileutil.device.Platform;
import com.kms.katalon.core.testobject.TestObject;

public class IOSComponent extends MobileComponent {

    public IOSComponent(TestObject testObject) {
        super(testObject);
    }

    @Override
    public Platform getPlatform() {
        return Platform.IOS;
    }
}
