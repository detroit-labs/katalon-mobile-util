package com.detroitlabs.katalonmobileutil.exception;

import java.util.NoSuchElementException;

import com.detroitlabs.katalonmobileutil.logging.Logger;

public class ListItemsNotFoundException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public ListItemsNotFoundException(String xpath) {
		super(formatMessage(xpath, null, null, null, null));
	}	
	
	public ListItemsNotFoundException(String xpath, String labelText) {
		super(formatMessage(xpath, null, null, null, labelText));
	}
	
	public ListItemsNotFoundException(String xpath, String resourceId, String resourceType) {
		super(formatMessage(xpath, resourceId, resourceType, null, null));
	}

	public ListItemsNotFoundException(String xpath, String resourceId, String resourceType, Integer index) {
		super(formatMessage(xpath, resourceId, resourceType, index, null));
	}
	
	public ListItemsNotFoundException(String xpath, String resourceId, String resourceType, String labelText) {
		super(formatMessage(xpath, resourceId, resourceType, null, labelText));
	}	
	
	protected static String formatMessage(String xpath, String resourceId, String resourceType, Integer index, String labelText) {
		String message = "\nCould not find any list elements matching xpath: " + xpath;
		if (resourceId != null) {
				message += "\nCheck that you have elements with " + resourceType + ": '" + resourceId + "'";
		}
		if (index != null) {
			message += "\n and that there are at least " + (index + 1) + " elements in the list";
		}
		if (labelText != null) {
			message += "\n and that exactly one label has the text: '" + labelText + "'" ;
		}		
		Logger.error(message);
		return message;
	}
}
