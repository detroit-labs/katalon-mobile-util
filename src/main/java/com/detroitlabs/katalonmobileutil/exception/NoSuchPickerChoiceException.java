package com.detroitlabs.katalonmobileutil.exception;

import java.util.List;
import java.util.NoSuchElementException;

public class NoSuchPickerChoiceException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public NoSuchPickerChoiceException(List<String> pickerChoices) {
		super(formatMessage(pickerChoices));
	}
	
	protected static String formatMessage(List<String> pickerChoices) {
		return "Could not find picker choices of: '" + pickerChoices + "'";
	}
	
}
