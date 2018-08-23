package com.detroitlabs.katalonmobileutil.exception;

import java.util.NoSuchElementException;

import com.detroitlabs.katalonmobileutil.logging.Logger;

public class ListItemsNotFoundException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public ListItemsNotFoundException(String xpath, String resourceId, String resourceType) {
		super(formatMessage(xpath, resourceId, resourceType, null));
	}

	public ListItemsNotFoundException(String xpath, String resourceId, String resourceType, Integer index) {
		super(formatMessage(xpath, resourceId, resourceType, index));
	}
	
	protected static String formatMessage(String xpath, String resourceId, String resourceType, Integer index) {
		String message = "\nCould not find any list elements matching xpath: " + xpath + 
				"\nCheck that you have elements with " + resourceType + ": '" + resourceId + "'";
		if (index != null) {
			message += "\n and that there are at least " + (index + 1) + " elements in the list";
		}
		Logger.error(message);
		return message;
	}
}
