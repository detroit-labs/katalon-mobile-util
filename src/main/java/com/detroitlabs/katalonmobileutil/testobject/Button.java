package com.detroitlabs.katalonmobileutil.testobject;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;
import com.kms.katalon.core.model.FailureHandling;
import com.kms.katalon.core.testobject.TestObject;

public class Button {

	private static FailureHandling defaultFailureHandling;
	private static Integer defaultTimeout;
	
	public static void initialize(int timeoutInSeconds, FailureHandling failureHandling) {
		defaultFailureHandling = failureHandling;
		defaultTimeout = timeoutInSeconds;
	}
	
	public static void tap(String name) {
		TestObject button = Finder.findButton(name);
		Button.tap(button);
	}
	
	public static void tap(String name, int timeoutInSeconds) {
		TestObject button = Finder.findButton(name);
		Button.tap(button, timeoutInSeconds);
	}
	
	public static void tap(String name, FailureHandling failureHandling) {
		TestObject button = Finder.findButton(name);
		Button.tap(button, failureHandling);
	}	
	
	public static void tap(String name, int timeoutInSeconds, FailureHandling failureHandling) {
		TestObject button = Finder.findButton(name);
		Button.tap(button, timeoutInSeconds, failureHandling);
	}
	
	public static void tap(TestObject button) {
		int timeoutInSeconds = defaultTimeout != null ? defaultTimeout : 0;
		FailureHandling failureHandling = defaultFailureHandling != null ? defaultFailureHandling : FailureHandling.STOP_ON_FAILURE;
		Button.tap(button, timeoutInSeconds, failureHandling);
	}
	
	public static void tap(TestObject button, int timeoutInSeconds) {
		FailureHandling failureHandling = defaultFailureHandling != null ? defaultFailureHandling : FailureHandling.STOP_ON_FAILURE;
		Button.tap(button, timeoutInSeconds, failureHandling);
	}
	
	public static void tap(TestObject button, FailureHandling failureHandling) {
		int timeoutInSeconds = defaultTimeout != null ? defaultTimeout : 0;
		Button.tap(button, timeoutInSeconds, failureHandling);
	}	
	
	public static void tap(TestObject button, int timeoutInSeconds, FailureHandling failureHandling) {
		if (Device.isWeb()) {
			WebUiBuiltInKeywords.click(button, failureHandling);
		}
		else {
			MobileBuiltInKeywords.tap(button, timeoutInSeconds, failureHandling);
		}
	}	
	
}
