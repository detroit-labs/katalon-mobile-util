package com.detroitlabs.katalonmobileutil.logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;
import com.kms.katalon.core.testobject.TestObject;
import com.kms.katalon.core.testobject.TestObjectProperty;

import io.appium.java_client.AppiumDriver;

public class Logger {

	// In order of view preference
	public enum LogLevel {
		DEBUG, INFO, WARN, ERROR, FATAL, OFF
	}
	
	private static PrintWriter writer;
	private static LogLevel baseLevel;

	public static void initialize(String logFilePath, LogLevel level) throws FileNotFoundException {
		writer = new PrintWriter(logFilePath);
		String startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		writer.println(String.format("Initializing log: %s with logging level: %s", startDate, level ));
		writer.flush();
		baseLevel = level;
	}
	
	protected void finalize() {
		writer.close();
	}
	
	public static void debug(String text) {
		log(text, LogLevel.DEBUG);
	}
	
	public static void info(String text) {
		log(text, LogLevel.INFO);
	}
	
	public static void warn(String text) {
		log(text, LogLevel.WARN);
	}
	
	public static void error(String text) {
		log(text, LogLevel.ERROR);
	}
	
	public static void fatal(String text) {
		log(text, LogLevel.FATAL);
	}
	
	/**
	 * Log the properties of the TestObject
	 * @param testObject TestObject for logging
	 * @param level LogLevel at which to do the logging. A LogLevel at or above the Logger's initialized value means that the logging will occur.
	 */
	public static void printTestObject(TestObject testObject, LogLevel level) {
		Logger.log(String.format("Test Object: %s", testObject.getObjectId()), level);
		List<TestObjectProperty> props = testObject.getProperties();
		for (TestObjectProperty p : props) {
			Logger.log(String.format("    %s: %s (active? %s)", p.getName(), p.getValue(), p.isActive()), level);
		}
	}
	
	/**
	 * Log the entire XML structure of what is on the screen currently.
	 * @param level LogLevel at which to do the logging. A LogLevel at or above the Logger's initialized value means that the logging will occur.
	 */
	public static void printScreenContents(LogLevel level) {
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
		Logger.log(driver.getPageSource(), level);
	}
	
	public static void log(String text, LogLevel level) {
		if (writer == null) {
			System.out.println("WARNING: Logger is not initialized. No log file will be written.");
			System.out.println(String.format("Would have logged: %s", text));
		} else {
			// Only write to the log at a level at or above the initialized base level
			if (level.compareTo(baseLevel) >= 0) {
				writer.println(String.format("[%s] %s: %s", level, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), text));
				writer.flush();
			}
		}
	}
	
}
