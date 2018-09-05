package com.detroitlabs.katalonmobileutil.exception;

public class AppNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AppNotFoundException(String platform) {
		super(formatMessage(platform));
	}

	protected static String formatMessage(String platform) {
		return "Could not find a matching app for platform: " + platform;
	}	
	
}
