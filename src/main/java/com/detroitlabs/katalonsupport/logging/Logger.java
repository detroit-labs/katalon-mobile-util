package com.detroitlabs.katalonsupport.logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	// In order of view preference
	public enum LogLevel {
		DEBUG, INFO, WARN, ERROR, FATAL, OFF
	}
	
	private static PrintWriter writer;
	private static LogLevel baseLevel;

	public static void initialize(String logFilePath, LogLevel level) throws FileNotFoundException {
		writer = new PrintWriter(logFilePath);
		writer.println(String.format("Initializing log: %s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) );
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
