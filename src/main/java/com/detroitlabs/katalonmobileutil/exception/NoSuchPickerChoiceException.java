package com.detroitlabs.katalonmobileutil.exception;

import java.util.NoSuchElementException;

public class NoSuchPickerChoiceException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public NoSuchPickerChoiceException(String pickerChoice) {
		super(formatMessage(pickerChoice));
	}
	
	protected static String formatMessage(String pickerChoice) {
		return "Could not find a picker choice of: '" + pickerChoice + "'";
	}
	
}
