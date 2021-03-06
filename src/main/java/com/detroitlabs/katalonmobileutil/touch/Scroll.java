package com.detroitlabs.katalonmobileutil.touch;

import java.util.*;

import com.detroitlabs.katalonmobileutil.testobject.TestObjectConverter;
import com.kms.katalon.core.testobject.TestObject;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.detroitlabs.katalonmobileutil.exception.ListItemsNotFoundException;
import com.detroitlabs.katalonmobileutil.logging.Logger;
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel;
import com.detroitlabs.katalonmobileutil.testobject.XPathBuilder;
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

public class Scroll {

	public enum ScrollFactor {
		SMALL(25),
		MEDIUM(50),
        LARGE(75),
		XLARGE(100);

		public final int factor;

		ScrollFactor(int factor) {
			this.factor = factor;
		}
	}

	public enum ScrollDirection {
		UP, DOWN
	}

	// used to determine when we've hit the end of the list and should stop attempting to scroll
	static String lastScrolledElement = null;

	// controls how much of the list to scroll on each "swipe"
	private static ScrollFactor baseScrollFactor = ScrollFactor.MEDIUM;

    /**
     * Set the default scrollFactor for all subsequent calls to Scroll functions.
     * Default is ScrollFactor.MEDIUM.
     * @param scrollFactor new default value for scrollFactor when performing scrolling.
     */
	public static void initialize(ScrollFactor scrollFactor) {
		baseScrollFactor = scrollFactor;
	}

	/**
	 * Scrolls through a list of a specific collection of elements on the screen, attempting to find the element at the
	 * given index.
	 *
	 * @param elementId identifier of the collection of text elements to scroll (Accessibility id/name for iOS and resource-id for Android).
	 * @param index index for the element in the list to find. NOTE: This index starts at 1.
	 * @param timeout delay in seconds between each scroll action.
	 * @return Katalon Test Object that was found or null if not found.
	 */
	public static TestObject scrollListToElementAtIndex(String elementId, Integer index, Integer timeout) {

		String xpath = "";

		if (Device.isIOS()) {
			xpath = XPathBuilder.createXPath("XCUIElementTypeStaticText");
			// iOS lists may have all elements in them from the start, even if not visible
			xpath = XPathBuilder.addVisible(xpath);
		} else if (Device.isAndroid()) {
			xpath = XPathBuilder.createXPath("TextView");
		} else {
			throw new UnsupportedOperationException("Device type is not supported.");
		}

		if (elementId != null) {
			xpath = XPathBuilder.addResourceId(xpath, elementId);
		}

		return scrollListToElementAtIndex(xpath, index, ScrollFactor.MEDIUM, timeout);
	}

	/**
	 * Scrolls through a list of all text elements on the screen, attempting to find
	 * the requested text. Throws an exception if the text is not found. Android
	 * only. 
	 * <p>
	 * For iOS, it is preferred to use scrollListToElementWithText(String elementId, String elementText, Integer timeout)
	 * <p>
	 * WARNING: In Android, buttons are also TextViews, so if a resource-id is not specified, then
	 * a footer button might be considered part of the scroll list, which could prevent scrolling
	 * of the actual list elements. It is always best practice to include a resource-id if possible.
	 * 
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param timeout delay in seconds between each scroll action.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementText, Integer timeout) {
		return scrollListToElementWithText(elementText, baseScrollFactor, timeout);
	}

	/**
	 * Scrolls through a list of all text elements on the screen, attempting to find
	 * the requested text. Throws an exception if the text is not found. Android
	 * only.
	 * <p>
	 * For iOS, it is preferred to use scrollListToElementWithText(String elementId, String elementText, Integer timeout)
	 * <p>
	 * WARNING: In Android, buttons are also TextViews, so if a resource-id is not specified, then
	 * a footer button might be considered part of the scroll list, which could prevent scrolling
	 * of the actual list elements. It is always best practice to include a resource-id if possible.
	 *
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param scrollFactor how big the scroll action should be before releasing, from SMALL to XLARGE
	 * @param timeout delay in seconds between each scroll action.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementText, ScrollFactor scrollFactor, Integer timeout) {
		if (Device.isIOS() || Device.isAndroid()) {
			return scrollListToElementWithText(null, elementText, scrollFactor, timeout);
		}

		throw new UnsupportedOperationException("Device type is not supported.");

	}
	
	/**
	 * Scrolls through a list of a specific collection of elements on the screen, attempting to find the requested text.
	 * Throws an exception if the text is not found. 
	 * @param elementId identifier of the collection of text elements to scroll (Accessibility id/name for iOS and resource-id for Android).
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param timeout delay in seconds between each scroll action.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementId, String elementText, Integer timeout) {
		return scrollListToElementWithText(elementId, elementText, baseScrollFactor, timeout);
	}

	/**
	 * Scrolls through a list of a specific collection of elements on the screen, attempting to find the requested text.
	 * Throws an exception if the text is not found.
	 * @param elementId identifier of the collection of text elements to scroll (Accessibility id/name for iOS and resource-id for Android).
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param scrollFactor how big the scroll action should be before releasing, from SMALL to XLARGE
	 * @param timeout delay in seconds between each scroll action.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementId, String elementText, ScrollFactor scrollFactor, Integer timeout) {
		String xpath = "";

		if (Device.isIOS()) {
			xpath = XPathBuilder.createXPath("XCUIElementTypeStaticText");
			// iOS lists may have all elements in them from the start, even if not visible
			xpath = XPathBuilder.addVisible(xpath);
		} else if (Device.isAndroid()) {
			xpath = XPathBuilder.createXPath("TextView");
		} else {
			throw new UnsupportedOperationException("Device type is not supported.");
		}

		if (elementId != null) {
			xpath = XPathBuilder.addResourceId(xpath, elementId);
		}

		return scrollListToElementWithXPath(xpath, elementText, scrollFactor, ScrollDirection.DOWN, timeout);

	}

	/**
	 * Scrolls through a list of a specific collection of elements on the screen, attempting to find the requested text.
	 * Throws an exception if the text is not found.
	 * @param elementId identifier of the collection of text elements to scroll (Accessibility id/name for iOS and resource-id for Android).
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param scrollFactor how big the scroll action should be before releasing, from SMALL to XLARGE
	 * @param scrollDirection which way the scrolling of the list should go, DOWN or UP
	 * @param timeout delay in seconds between each scroll action.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToElementWithText(String elementId, String elementText, ScrollFactor scrollFactor, ScrollDirection scrollDirection, Integer timeout) {
		String xpath = "";

		if (Device.isIOS()) {
			xpath = XPathBuilder.createXPath("XCUIElementTypeStaticText");
			// iOS lists may have all elements in them from the start, even if not visible
			xpath = XPathBuilder.addVisible(xpath);
		} else if (Device.isAndroid()) {
			xpath = XPathBuilder.createXPath("TextView");
		} else {
			throw new UnsupportedOperationException("Device type is not supported.");
		}

		if (elementId != null) {
			xpath = XPathBuilder.addResourceId(xpath, elementId);
		}

		return scrollListToElementWithXPath(xpath, elementText, scrollFactor, scrollDirection, timeout);

	}

	/**
	 * Scrolls through a list of all checkbox elements on the screen, attempting to find
	 * the requested text. Throws an exception if the text is not found.
	 * <p>
	 * For iOS, it is preferred to use scrollListToElementWithText(String elementId, String elementText, Integer timeout)
	 * so that an elementId can be specified, narrowing the search.
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param timeout delay in seconds between each scroll action. 
	 * @return true if the text was found.
	 */
	public static boolean scrollListToCheckboxWithText(String elementText, Integer timeout) {
		return scrollListToCheckboxWithText(elementText, baseScrollFactor, timeout);
	}

	/**
	 * Scrolls through a list of all checkbox elements on the screen, attempting to find
	 * the requested text. Throws an exception if the text is not found.
	 * <p>
	 * For iOS, it is preferred to use scrollListToElementWithText(String elementId, String elementText, Integer timeout)
	 * so that an elementId can be specified, narrowing the search.
	 * @param elementText text to attempt to find in the scrolling list.
	 * @param scrollFactor how big the scroll action should be before releasing, from SMALL to XLARGE
	 * @param timeout delay in seconds between each scroll action.
	 * @return true if the text was found.
	 */
	public static boolean scrollListToCheckboxWithText(String elementText, ScrollFactor scrollFactor, Integer timeout) {
		String xpath = "";

		if (Device.isIOS()) {
			// iOS Checkboxes can be referenced by their Labels
			xpath = XPathBuilder.createXPath("XCUIElementTypeStaticText");
			// iOS lists may have all elements in them from the start, even if not visible
			xpath = XPathBuilder.addVisible(xpath);
		} else if (Device.isAndroid()) {
			xpath = XPathBuilder.createXPath("CheckBox");
		} else {
			throw new UnsupportedOperationException("Device type is not supported.");
		}

		return scrollListToElementWithXPath(xpath, elementText, scrollFactor, ScrollDirection.DOWN, timeout);
	}

	private static boolean scrollListToElementWithXPath(String xpath, String elementText, ScrollFactor scrollFactor, ScrollDirection scrollDirection, Integer timeout) {
		boolean isElementFound = false;
		while (isElementFound == false) {
			try {
				Logger.debug("Checking for specific element: " + elementText);
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();
				String xpathWithText = XPathBuilder.addLabel(xpath, elementText);
				Logger.debug("Trying to find xpath with text: " + xpathWithText);
				driver.findElementByXPath(xpathWithText);
				isElementFound = true;
				Logger.debug("Found an element in the current scroll list.");
				// reset the last scrolled element for the next time we do scrolling
				lastScrolledElement = null;
			} catch (WebDriverException ex) {
				// In this case, we're using the WebDriverException to trigger the scroll event, so it's ok that it occurs.
				Logger.debug("Didn't find any matching elements.");
				scrollEntireList(xpath, elementText, scrollFactor, scrollDirection, timeout);
			}
		}
		return isElementFound;
	}

	private static TestObject scrollListToElementAtIndex(String xpath, Integer index, ScrollFactor scrollFactor, Integer timeout) {

		final Set<String> runningList = new LinkedHashSet<>();

		boolean isIndexFound = false;
		TestObject elementAtIndex = null;
		while (isIndexFound == false) {
			try {
				Logger.debug("Checking scroll list for index: " + index);
				AppiumDriver<?> driver = MobileDriverFactory.getDriver();

				driver.findElementsByXPath(xpath).forEach(rwe->runningList.add(rwe.getText()));
				Logger.debug("Running List so far: " + runningList);
				if (runningList.size() >= index) {
					isIndexFound = true;
					String textAtIndex = runningList.toArray()[index - 1].toString(); // arrays are 0 based
					Logger.debug("Found element at index: " + index + " : " + textAtIndex);

					// reset the last scrolled element for the next time we do scrolling
					lastScrolledElement = null;

					// return the found element at index, since the full list and index aren't available in the calling test
					// need to look up the found element by text (or we could keep track of the elements in a hash by text)
					String xpathWithLabel = XPathBuilder.addLabel(xpath, textAtIndex);
					elementAtIndex = TestObjectConverter.fromElement((RemoteWebElement)driver.findElementByXPath(xpathWithLabel));

				} else {
					scrollEntireList(xpath, null, scrollFactor, ScrollDirection.DOWN, timeout);
				}
			} catch (WebDriverException ex) {
				// In this case, we're using the WebDriverException to trigger the scroll event, so it's ok that it occurs.
				Logger.debug("Didn't find any matching elements yet.");
				scrollEntireList(xpath, null, scrollFactor, ScrollDirection.DOWN, timeout);
			}
		}
		return elementAtIndex;
	}

	private static void scrollEntireList(String xpath, String elementText, ScrollFactor scrollFactor, ScrollDirection scrollDirection, Integer timeout) {
		AppiumDriver<?> driver = MobileDriverFactory.getDriver();
			
		Logger.debug("Getting a scroll list of all elements.");
		
		@SuppressWarnings("unchecked")
		List<RemoteWebElement> listElements = (List<RemoteWebElement>) driver.findElementsByXPath(xpath);
		
		if (listElements.size() <= 0) {
			// throw a new exception for not being able to find the elements
			throw(new ListItemsNotFoundException(xpath));
		} 
		
		Logger.debug("Current list:");
		Logger.printList(listElements, LogLevel.DEBUG);
			
		// TODO: Handle offset when looking at header labels with a lot of subitems in between
		RemoteWebElement bottomElement = listElements.get(listElements.size() - 1);	
		
		// It's possible that the very last element may be cut off and cannot be tapped.
		// In this case, the last visible list item should be considered the bottom.
		RemoteWebElement justAboveBottomElement = listElements.get(listElements.size() - 2);
		if (bottomElement.getSize().height < justAboveBottomElement.getSize().height) {
			bottomElement = justAboveBottomElement;
		}
		RemoteWebElement topElement = listElements.get(0);
		Logger.debug("Top element is now: " + topElement.getText() + " -> " + topElement.getLocation());
		
		Logger.debug("Bottom element is now: " + bottomElement.getText() + " -> " + bottomElement.getLocation());	
		
		// Check if the last element is the same as the previous time we scrolled, if so,
		// it means we hit the end of the list without finding the element
		// and should throw an error.
		String boundaryElementText = scrollDirection == ScrollDirection.DOWN ? "last" : "first";
		Logger.debug("Comparing the previous " + boundaryElementText + " element in the list: " + lastScrolledElement);
		RemoteWebElement boundaryElement = scrollDirection == ScrollDirection.DOWN ? bottomElement : topElement;
		Logger.debug("with the new " + boundaryElementText + " element in the list: " + (boundaryElement != null ? boundaryElement.getText() : "null"));
		if (lastScrolledElement != null && lastScrolledElement.equals(boundaryElement.getText())) {
			Logger.error("Scrolled to the end of the list and we didn't find the element.");
			// reset the last scrolled element for the next time we do scrolling
			lastScrolledElement = null;
			throw(new ListItemsNotFoundException(xpath, elementText));
		}
		
		Logger.debug("Resetting lastScrolledElement to " + boundaryElement.hashCode() + " " + boundaryElement.getText());
		lastScrolledElement = boundaryElement.getText();
		
		// Press and scroll from the last element in the list all the way to the top
		Logger.debug("Scrolling...");
		TouchAction touchAction = new TouchAction(driver);
		Point from = scrollDirection == ScrollDirection.DOWN ? bottomElement.getLocation() : topElement.getLocation();
		Point to = scrollDirection == ScrollDirection.DOWN ? topElement.getLocation() : bottomElement.getLocation();

		// This simulates a swipe action, so releasing at the top of the screen will
		// scroll the screen way further than we want. We may need to release the press
		// further down the screen. Allowing the tester to set a scrollFactor will give them more control
		// over how far the list scrolls.
		int endY = from.y - (int)((from.y - to.y) * (double)scrollFactor.factor / 100.0);
		Logger.debug("Scrolling from " + from.y + " to " + endY + " using scrollFactor " + scrollFactor);
		touchAction.longPress(PointOption.point(from.x, from.y)).moveTo(PointOption.point(to.x, endY)).release().perform();
		
		// Sometimes need a delay after scrolling before checking for the element
		MobileBuiltInKeywords.delay(timeout);
		
		Logger.debug("********** Scroll attempt complete *********** ");
		
	}		
	
}
