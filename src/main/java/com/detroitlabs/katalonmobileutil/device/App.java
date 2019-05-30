package com.detroitlabs.katalonmobileutil.device;

import com.detroitlabs.katalonmobileutil.exception.PlatformNotFoundException;

public class App {

	public String filePath;
	public String appId;
	public Platform platform;

	public App(String filePath, String appId) throws PlatformNotFoundException {
		this.filePath = filePath;
		this.appId = appId;

		Platform platform = null;
		if (filePath.endsWith(".ipa") || filePath.endsWith(".app")) {
			platform = Platform.IOS;
		} else if (filePath.endsWith(".apk")) {
			platform = Platform.ANDROID;
		} else {
			throw new PlatformNotFoundException(this);
		}
		this.platform = platform;
	}

	public App(String filePath, String appId, Platform platform) {
		this.filePath = filePath;
		this.appId = appId;
		this.platform = platform;
	}
	
	public String toString() {
		return "path:           " + filePath + "\n"
			 + "application id: " + appId;
	}

}
