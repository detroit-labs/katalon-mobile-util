package com.detroitlabs.katalonmobileutil.testobject;

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
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
		int timeoutInSeconds = defaultTimeout != null ? defaultTimeout : 0;
		FailureHandling failureHandling = defaultFailureHandling != null ? defaultFailureHandling : FailureHandling.STOP_ON_FAILURE;
		Button.tap(name, timeoutInSeconds, failureHandling);
	}
	
	public static void tap(String name, int timeoutInSeconds) {
		FailureHandling failureHandling = defaultFailureHandling != null ? defaultFailureHandling : FailureHandling.STOP_ON_FAILURE;
		Button.tap(name, timeoutInSeconds, failureHandling);
	}
	
	public static void tap(String name, FailureHandling failureHandling) {
		int timeoutInSeconds = defaultTimeout != null ? defaultTimeout : 0;
		Button.tap(name, timeoutInSeconds, failureHandling);
	}	
	
	public static void tap(String name, int timeoutInSeconds, FailureHandling failureHandling) {
		TestObject button = Finder.findButton(name);
		MobileBuiltInKeywords.tap(button, timeoutInSeconds, failureHandling);
	}
	
}
