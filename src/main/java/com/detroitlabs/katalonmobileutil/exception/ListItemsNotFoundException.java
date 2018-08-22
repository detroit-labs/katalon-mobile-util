package com.detroitlabs.katalonmobileutil.exception;

import java.util.NoSuchElementException;

public class ListItemsNotFoundException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public ListItemsNotFoundException(String xpath, String resourceId, String resourceType) {
		super("\nCould not find any list elements matching xpath: " + xpath + "\nCheck that you have elements with " + resourceType + ": '" + resourceId + "'");
	}

}
