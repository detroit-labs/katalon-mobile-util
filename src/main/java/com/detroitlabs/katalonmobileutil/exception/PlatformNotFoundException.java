package com.detroitlabs.katalonmobileutil.exception;

import com.detroitlabs.katalonmobileutil.device.App;

public class PlatformNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PlatformNotFoundException(App app) {
		super(formatMessage(app));
	}

	protected static String formatMessage(App app) {
		return "Could not determine the platform of the app file:\n" + app;
	}
	
}
