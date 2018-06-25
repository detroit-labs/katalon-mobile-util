package com.detroitlabs.katalonsupport.device;

import java.util.Map;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;

public class Device {
	
	public Device() {
		
	}
	
	public boolean isIOS() {
		return "ios".equals(this.getDeviceOS().toLowerCase());
	}

	public boolean isAndroid() {
		return "android".equals(this.getDeviceOS().toLowerCase());	
	}

	public void startApp(String iOSFile, String androidFile) {
		this.startApp(iOSFile, androidFile, false);
	}
	
	public void startApp(String iOSFile, String androidFile, boolean resetSimulator) {
		String appFile = "ios".equals(this.getDeviceOS().toLowerCase()) ? iOSFile : androidFile;
		MobileBuiltInKeywords.startApplication(appFile, resetSimulator);
	}
	
	public String getDeviceOS() {
		ExecutionProperties props = new ExecutionProperties(RunConfiguration.getExecutionProperties());
		String deviceOS = props.getDeviceOS();
		return deviceOS;
	}
	
	private class ExecutionProperties {
		
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
