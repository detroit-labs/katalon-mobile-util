package com.detroitlabs.katalonmobileutil.device;

import java.util.List;
import java.util.Map;

import com.detroitlabs.katalonmobileutil.exception.AppNotFoundException;
import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;

public class Device {
	
	private static String bundleId;
	private static String appFile;
	
	public static boolean isIOS() {
		return "ios".equals(getDeviceOS().toLowerCase());
	}

	public static boolean isAndroid() {
		return "android".equals(getDeviceOS().toLowerCase());	
	}
	
	public static String getDeviceOS() {
		ExecutionProperties props = new ExecutionProperties(RunConfiguration.getExecutionProperties());
		String deviceOS = props.getDeviceOS();
		return deviceOS;
	}	
	
	/**
	 * Start a mobile application for testing, automatically determined by the device platform.
	 * @param apps a list of Apps for each platform that will be parsed when starting the app. The app for the current platform will be chosen.
	 * @param removeAppBefore determines whether app data will be cleared before the app starts.
	 * @throws AppNotFoundException 
	 */
	public static void startApp(List<App> apps, boolean removeAppBefore) throws AppNotFoundException {
		App app = null;
		for (App a : apps) {
			if (a.platform.toString().equalsIgnoreCase(Device.getDeviceOS())) {
				app = a;
				break;
			}
		}
		if (app == null) {
			throw new AppNotFoundException(Device.getDeviceOS());
		}
		startApp(app.filePath, app.appId, removeAppBefore);
		
	}
	
	/**
	 * Start a mobile application for testing.
	 * @param filePath absolute path to the .app, .ipa, or .apk file containing the application.
	 * @param appId application bundle identifier/package name. Used when uninstalling an app. Can be null if not uninstalling the app before or after the test.
	 * @param removeAppBefore determines whether app data will be cleared before the app starts.
	 */
	public static void startApp(String filePath, String appId, boolean removeAppBefore) {
		appFile = filePath;
		bundleId = appId;
		
		if (removeAppBefore == false) {
			MobileBuiltInKeywords.startApplication(appFile, removeAppBefore);
		} else {
			if (Device.isIOS()) {
				// if iOS, resetting before only takes the starting the app with the true flag
				MobileBuiltInKeywords.startApplication(appFile, removeAppBefore);
			} else {
				// if Android, resetting before requires us to remove the app in code and then start the test
				MobileBuiltInKeywords.startApplication(appFile, false);
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();
				driver.removeApp(bundleId);
				MobileBuiltInKeywords.startApplication(appFile, false);
			}
			
		}
	}	
	
	/**
	 * Stops the application, leaving the state intact. 
	 */
	public static void stopApp() {
		stopApp(false);
	}
	
	/**
	 * Stops the application, optionally uninstalling the app
	 * @param uninstallApp indicate whether the app should be removed after it is stopped. NOTE: On iOS, the app will not be removed, but its data will be cleared.
	 */
	public static void stopApp(boolean uninstallApp) {
		if (uninstallApp == false) {
			MobileBuiltInKeywords.closeApplication();
		} else {
			
			AppiumDriver<?> driver = MobileDriverFactory.getDriver();
			
			if (Device.isIOS()) {
				// Must have the bundle id or package name to uninstall an app
				driver.removeApp(bundleId);
				// Uninstalling an iOS app doesn't necessarily wipe out its data, so we need to reset the simulator, too.
				MobileBuiltInKeywords.startApplication(appFile, true);
				MobileBuiltInKeywords.closeApplication();
				
			} 
			else {
				// Must have the bundle id or package name to uninstall an app
				driver.removeApp(bundleId);
			}	
			
		}
		
	}
	
	private static class ExecutionProperties {
		
		Map<String, Object> properties;
		
		ExecutionProperties(Map<String, Object> properties) {
			this.properties = properties;
		}
		
		@SuppressWarnings("unchecked")
		String getDeviceOS() {
			Map<String, Object> drivers = (Map<String, Object>)(this.properties.get("drivers"));
			Map<String, Object> system = (Map<String, Object>)(drivers.get("system"));
			Map<String, String> mobile = (Map<String, String>)(system.get("Mobile"));
			String deviceOS = mobile.get("deviceOS");
			return deviceOS;
		}
	}
	
}
