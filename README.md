<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [katalon-mobile-util](#katalon-mobile-util)
  - [Javadocs](#javadocs)
  - [Installation](#installation)
  - [Building from source](#building-from-source)
    - [Prerequisites](#prerequisites)
    - [Building](#building)
  - [Installing from source](#installing-from-source)
  - [Usage](#usage)
    - [Device](#device)
      - [How to use Device](#how-to-use-device)
    - [TestObjects](#testobjects)
      - [Organization](#organization)
      - [How to use TestObject Finder](#how-to-use-testobject-finder)
      - [Finding a Label from a List by Index](#finding-a-label-from-a-list-by-index)
      - [Finding a Label from a List by the Label Text](#finding-a-label-from-a-list-by-the-label-text)
      - [Finding a Checkbox in a List by the Checkbox Text](#finding-a-checkbox-in-a-list-by-the-checkbox-text)
      - [Converting from Selenium WebElements to Katalon TestObjects](#converting-from-selenium-webelements-to-katalon-testobjects)
    - [Button](#button)
    - [TextField](#textfield)
      - [How to use TextField](#how-to-use-textfield)
      - [Using a TextField with a Picker list](#using-a-textfield-with-a-picker-list)
      - [Keyboard Handling](#keyboard-handling)
    - [Scrolling](#scrolling)
      - [How to use Scroll](#how-to-use-scroll)
      - [Scrolling more or less distance with ScrollFactor](#scrolling-more-or-less-distance-with-scrollfactor)
    - [Swiping](#swiping)
      - [How to use Swipe](#how-to-use-swipe)
    - [Logging](#logging)
      - [Log Levels](#log-levels)
      - [How to use Logger](#how-to-use-logger)
  - [Resources](#resources)
  - [Contact](#contact)
  - [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# katalon-mobile-util
[Katalon Studio](https://www.katalon.com/) is an IDE that provides a unified way to test UI for mobile and web. 

**katalon-mobile-util** is a library of utilities to make mobile UI testing in Katalon Studio easier.

## Javadocs

For detailed usage, view the [Javadocs](https://detroit-labs.github.io/katalon-mobile-util/).

## Installation

To use this **katalon-mobile-util** library in Katalon Studio tests, it is not required that you build from source.

Place the [release artifact jar](https://github.com/detroit-labs/katalon-mobile-util/releases/download/1.14.1/katalon-mobile-util-1.14.1.jar) into your Katalon test project's `/Drivers` directory, or follow the Katalon Studio instructions: [How to import external library into your automation project](https://www.katalon.com/resources-center/tutorials/import-java-library/).

After installation, make sure you restart Katalon Studio for the library to be loaded correctly.

## Building from source

Although not required, you may build the **katalon-mobile-util** library from source code. The resulting `.jar` will be added to your Katalon Studio test project.

### Prerequisites

This library requires [Katalon Studio version 7.x](https://www.katalon.com/) to be installed.

Building from source requires [Apache Maven](https://maven.apache.org/).

### Building

The Katalon Studio jar files are not available via Maven Central and are not packaged with the **katalon-mobile-util** library, so we can set up a local maven repository to contain the required files:

Run the script to move the jar files from Katalon Studio to the `lib` directory:

```
cd katalon-mobile-util
./scripts/install-dependencies.sh
```

Build the katalon-mobile-util package:

```
mvn package
```

The resulting `.jar` file will be created in the **katalon-mobile-util** library's `target` directory.

## Installing from source

1. Edit the **katalon-mobile-util** project's `pom.xml` file to set `project.target.katalon.directory` to your Katalon Studio test project's `/Drivers` directory.

2. Follow the steps for "Building from source".

3. Build and install the package:

```
mvn install -U
```

4. The **katalon-mobile-util** `.jar` file will be placed in your Katalon Studio test project's `/Drivers` directory.

## Usage

This **katalon-mobile-util** library provides convenience functions for interacting with Katalon Studio and Appium features:

For detailed usage, view the [Javadocs](https://detroit-labs.github.io/katalon-mobile-util/).

### Device

Provides information about the platform of the device on which the tests are running. This allows a single set of tests to perform branching logic between iOS and Android without the need to create separate test suites.

#### How to use Device

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.device.App
import com.detroitlabs.katalonmobileutil.device.Device
```

Start the test application on the device. Provide both the iOS and Android files and use the device platform to determine which file to test.  
Set `removeAppBeforeTest` below to clear the app before running the test. 

```
String androidFile = '~/Downloads/mobile-beta.apk'
String androidAppId = 'com.mycompany.myapp'
App androidApp = new App(androidFile, androidAppId)

String iosFile = '~/Downloads/mobile-beta.app'
String iosAppId = 'com.mycompany.myapp'
App iosApp = new App(iosFile, iosAppId)

boolean removeAppBeforeTest = true // change this to false to keep the app state between tests
Device.startApp([iosApp, androidApp], removeAppBeforeTest)
```

Prints the platform for the test device, `iOS` or `Android`

```
println(Device.getDeviceOS())
```

Determines the platform of the test device and allows for branching logic

```
if (Device.isIOS()) {
  println("This is an iOS device.")
}
if (Device.isAndroid()) {
  println("This is an Android device.")
}
```

Stop and clear the app data from the device:  
NOTE: On iOS, app will not be uninstalled, but its data will be cleared.

```
boolean uninstallApp = true
Device.stopApp(uninstallApp)
```

### TestObjects

#### Organization

The basic elements of Katalon Studio tests are [TestObjects](https://docs.katalon.com/display/KD/Manage+Test+Object). These objects are stored in the Object Repository. To keep things consistently organized, **katalon-mobile-util** assumes that the following structure will be used to store `TestObjects`:

![Object Repository](/../screenshots/img/object_repository.png?raw=true "Object Repository")

Because iOS and Android `TestObject` properties vary slightly, using "iOS Objects" and "Android Objects" folders allows **katalon-mobile-util** to dynamically switch between the `TestObjects` based on the test device platform. Store the `TestObject` with the same name for each platform, e.g. `Checkout button` and **katalon-mobile-util** will pick the correct object for the platform.

#### How to use TestObject Finder

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.testobject.Finder
```

Find a `TestObject` from the Object Repository:

```
TestObject alert = Finder.findAlert('My Alert')
TestObject button = Finder.findButton('My Button')
TestObject checkbox = Finder.findCheckbox('My Checkbox')
TestObject image = Finder.findImage('My Image')
TestObject label = Finder.findLabel('My Label')
TestObject link = Finder.findLink('My Link')
TestObject segmentedControl = Finder.findSegmentedControl('My Segmented Control')
TestObject switch = Finder.findSwitch('My Switch')
TestObject tab = Finder.findTab('My Tab')
TestObject textField = Finder.findTextField('My Text Field')
```

If the `TestObject` doesn't fall into one of those categories, you can save it in the top-level iOS or Android directory and use `findGeneric` to retrieve it: 

```
TestObject genericObject = Finder.findGeneric('My Uncategorized Generic Object')
```

Once you have the `TestObject`, you can interact with it per the normal Katalon `MobileBuiltInKeywords`:

```
MobileBuiltInKeywords.tap(button, timeout)
```

If you want to store `TestObjects` in a non-default directory, you can override the repository locations:

```
Finder.setIOSRepository('My Custom iOS Folder')
Finder.setAndroidRepository('My Custom Android Folder')
```

TIP: All of the available TestObject types can be used by importing:

```
import com.detroitlabs.katalonmobileutil.testobject.TestObjectType

// Gives you access to:
TestObjectType.ALERT
TestObjectType.CHECKBOX
TestObjectType.LABEL
// etc.
```

#### Finding an Element from a List by Index

In iOS, we can't specify an `accessibility id` for a `UITableView`, but we can use the `accessibility id` assigned to similar elements in the table view to find a specific element.

To find a specific element from a collection of similar elements on the screen, first create a 
new TestObject in the Object Repository in the appropriate directory for its type. The new object should have properties for 
the `type` and `name` for iOS (or `class` and `resource-id` for Android). 

TIP: You should leave off the `label` or `text` properties in order to keep the Label generic to 
represent the whole collection of elements.

##### Generic iOS Test Object
![Generic iOS Label](/../screenshots/img/generic_label_ios.png?raw=true "Generic iOS Label")

##### Generic Android Test Object
![Generic Android Label](/../screenshots/img/generic_label_android.png?raw=true "Generic Android Label")

Then provide an index for which element in the list you want to find (indexes start at 1):

```
import com.detroitlabs.katalonmobileutil.testobject.TestObjectType

int index = 3 // first element in the list is at index 1, so this gets the 3rd element
TestObject labelAtIndex = Finder.findElementAtIndex(TestObjectType.LABEL, 'Generic label element', index) 
```

#### Finding a Label from a List by the Label Text

In iOS, we can't specify an `accessibility id` for a `UITableView`, but we can use the `accessibility id` assigned to similar labels in the table view to find a specific element.

To find a specific label from a collection of similar labels on the screen, first create a 
new Label TestObject in the Object Repository. The new Label should have properties for 
the `type` and `name` for iOS (or `class` and `resource-id` for Android).

You should leave off the `label` or `text` properties in order to keep the Label generic to 
 represent the whole collection of labels. 


##### Generic iOS Test Object
![Generic iOS Label](/../screenshots/img/generic_label_ios.png?raw=true "Generic iOS Label")

##### Generic Android Test Object
![Generic Android Label](/../screenshots/img/generic_label_android.png?raw=true "Generic Android Label")

Then provide the text for the specific label you want to find:

```
TestObject labelWithText = Finder.findLabelWithText('Generic label element', 'Label Text') 
```

#### Finding a Checkbox in a List by the Checkbox Text

In Android, `CheckBoxes` do not necessarily have a `resource-id` and unlike in iOS, 
the label and checkbox are not separate. To find a Checkbox on screen based on its text, in either iOS or Android:

```
TestObject checkbox = Finder.findCheckboxWithText('Checkbox Text') 
```

#### Converting from Selenium WebElements to Katalon TestObjects

The base components of Selenium tests are `RemoteWebElements`, which are extended to `WebElements` by Selenium and `MobileElements` by Appium. The base components for Katalon Studio are `TestObjects`. Sometimes it may be useful to convert from a `MobileElement` that you found with Appium functions, to a `TestObject` for use in Katalon tests so that you can use Katalon functions on it.

Add this import statement to your test file:

```
import org.openqa.selenium.By
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.testobject.TestObject
import com.detroitlabs.katalonmobileutil.testobject.TestObjectConverter
```

Use an Appium function to get some `MobileElements` matching an xpath:

```
AppiumDriver<MobileElement> driver = (AppiumDriver<MobileElement>) MobileDriverFactory.getDriver()
List<MobileElement> mobileElements = driver.findElements(By.xpath(xpathValue))
```

Use katalon-mobile-util's `TestObjectConverter` to create `TestObjects` from each of the `MobileElements`:

```
List<TestObject> testObjects = TestObjectConverter.fromElements(mobileElements)
```

Or convert a single `MobileElement`:

```
TestObject testObject = TestObjectConverter.fromElement(mobileElements.get(0))
```

### Button

The typical flow for interacting with a button is:

1) Find the button from the TestObject Repository
2) Tap the button, providing a timeout and optional `FailureHandling` behavior in the case that the button doesn't exist.

For convenience, katalon-mobile-util allows you to interact with buttons in one step:

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.testobject.Button
```

In your test, tap the button using the name of the saved TestObject:

```
Button.tap('OK button')
```

By default, the timeout is 0 seconds and the test will stop with an error if the button is not found.

To control the behavior further, you can override the defaults, giving all future calls to `Button.tap()` the properties: 

```
int timeout = 3
Button.initialize(timeout, FailureHandling.OPTIONAL)
```

To add more control to an individual button tap, you can provide one or both of the `timeout` and `FailureHandling` arguments:

```
int timeout = 3
Button.tap('OK button', timeout)
Button.tap('OK button', FailureHandling.OPTIONAL)
Button.tap('OK button', timeout, FailureHandling.OPTIONAL)
```

### TextField

iOS and Android text fields are represented differently within the structure of a screen. iOS text fields are often directly accessible as `XCUIElementTypeTextField` with an `accessibility id` or (`name` as it is referred to in Katalon Studio `TestObjects`). We can interact with these fields using Katalon's `MobileBuiltInKeywords` class:

```
MobileBuiltInKeywords.setText(textFieldObject, 'Text to set', timeout)
```

Android text fields are sometimes auto-wrapped in an `android.widget.RelativeLayout`, where the `RelativeLayout` gets the `resource-id` reference, not the text field itself. Katalon's `MobileBuiltInKeywords.setText()` doesn't work with `RelativeLayout` objects.

**katalon-mobile-util** provides a wrapper to interact with text fields in both iOS or Android; the implementation is determined by the test device platform.

#### How to use TextField

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.testobject.TextField
```

If you are testing on an Android device, and the text fields have a clear button, be sure to create a `TestObject` for that button:

```
TestObject clearButton = Finder.findButton('Clear text field button')
```

Clear and set the text field:

```
int timeout = 10
TestObject streetAddress = Finder.findTextField('Street address form field')
TextField.clearText(streetAddress, timeout, clearButton)
TextField.typeText(streetAddress, '123 My Street', timeout)
```

For iOS, using a clear button is unnecessary, so it can be omitted from the call:

```
int timeout = 10
TestObject streetAddress = Finder.findTextField('Street address form field')
TextField.clearText(streetAddress, timeout)
TextField.typeText(streetAddress, '123 My Street', timeout)
```

#### Using a TextField with a Picker list

Some TextFields don't bring up the keyboard, but allow the user to select values from a predetermined picker or drop-down list instead.

To fill in a TextField by selecting its value from a list, e.g. selecting "Michigan" from a list of states to populate the TextField:

```
int timeout = 10
TestObject stateField = Finder.findTextField('State form field')
TextField.selectOption(stateField, 'Michigan', timeout)
```

To fill in a TextField that is built from multiple values, e.g. a multi-part date, provide a value for each part of the picker. Date picker selections often get transformed when displayed in the form, so we also provide the expected value of the field when the picker selections are made. If the values don't match, the test will fail.

```
TextField.selectOption(expirationDateField, ['September', '12', '2018'], '9/12/2018', timeout)
```

#### Keyboard Handling

To move between TextFields in a form and autofocus on the next field.  
**NOTE:** For iOS, this will look for the "Next" or ">" button on the keyboard toolbar. Android uses the keyboard's tab key.

```
TextField.nextField()
```

To close the keyboard.  
**NOTE:** For iOS, this will look for the "Done", "Select", or "OK" button on the keyboard toolbar. Closing the Android keyboard does not rely on these buttons.

```
TextField.hideKeyboard()
```

### Scrolling

Katalon Studio provides a method for [scrolling](https://docs.katalon.com/display/KD/%5BMobile%5D+Scroll+To+Text) a list until particular text is visible on the screen. Unfortunately, this function has proven problematic and results in erratic scrolling.

**katalon-mobile-util** provides a wrapper to scroll lists using the [Appium TouchAction](http://appium.io/docs/en/writing-running-appium/touch-actions/).

#### How to use Scroll

Add these imports statement to your test file:

```
import com.detroitlabs.katalonmobileutil.touch.Scroll
import com.detroitlabs.katalonmobileutil.touch.Scroll.ScrollFactor
```

Scroll list of all `XCUIElementTypeStaticText` (for iOS) or `*TextView` (for Android) elements until you get to the given text:

 ```
 int timeout = 1
 Scroll.scrollListToElementWithText('Michigan', timeout)
 ```

Where `timeout` is the delay between "swipes" when scrolling.

Scroll specific list of elements with the `accessibility id` (for iOS) or `resource-id` (for Android) until you get to the given text. In this case, all of the elements in the collection should have the same ids, e.g. `state_label`:

 ```
 Scroll.scrollListToElementWithText('state_label', 'Michigan', timeout)
 ```
 
 Once scrolling is complete, you can interact with the element, assuming you already have a TestObject named "Michigan label".
 
 ```
 Mobile.tap(Finder.findLabel('Michigan label'), timeout)
 ```
 
Scroll a list of `CheckBoxes`.  

```
Scroll.scrollListToCheckboxWithText('My Option 1', timeout)
```

#### Scrolling more or less distance with ScrollFactor

Depending on the height of the section to be scrolled, and the items within it, you may need to adjust how far
each scroll action travels. For example, if the list elements are not very tall, scrolling a large distance
may scroll some items off the screen so that they are not detected.

By using `ScrollFactor`, you can control the degree of scrolling:

```
Scroll.scrollListToElementWithText('Michigan', ScrollFactor.LARGE, timeout)
```

The following `ScrollFactors` are available:

```
ScrollFactor.SMALL   // Scrolls roughly 25% of the scroll area on each swipe
ScrollFactor.MEDIUM  // Scrolls roughly 50% of the scroll area on each swipe
ScrollFactor.LARGE   // Scrolls roughly 75% of the scroll area on each swipe
ScrollFactor.XLARGE  // Scrolls roughly 100% of the scroll area on each swipe
```

`ScrollFactor` is an optional parameter (it defaults to `ScrollFactor.MEDIUM`).

The default can be overridden by calling the `initialize()` function:

```
Scroll.initialize(ScrollFactor.SMALL)
```

All subsequent calls to `Scroll` functions during the test that don't provide a ScrollFactor will use the set `ScrollFactor`.

### Swiping

Swiping differs from Scrolling by ignoring the elements of the screen and performing an action as if the user was swiping
across the screen in a given direction. 

Available directions to swipe are `LEFT_TO_RIGHT`, `RIGHT_TO_LEFT`, `TOP_TO_BOTTOM`, and `BOTTOM_TO_TOP`.
(The first position is where the swipe action starts and the second position is where it will end, e.g. `BOTTOM_TO_TOP` 
will start a press at the bottom of the screen and release at the top of the screen, effectively scrolling down through a list).

#### How to use Swipe

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.touch.Swipe
import com.detroitlabs.katalonmobileutil.touch.Swipe.SwipeDirection
```

Swipe in a specific direction:

```
Swipe.swipe(SwipeDirection.BOTTOM_TO_TOP)
```

### Logging

Katalon Studio logs output from `println()` to the console, but it is often difficult to parse out your particular statement from the logs already being created by Appium and Katalon, with INFO and DEBUG being the only log level options. 

Katalon Studio also provides a way to write to the Log Viewer tab using [`KeywordLogger`](http://www.sanspantalones.com/2018/02/14/output-status-messages-and-test-information-by-writing-to-the-log-file-viewer-in-katalon-studio/). However, the logged statements are mixed in with the test results in this case. 

**katalon-mobile-util** provides a cleaner way to log your own events to a file of your choosing. 

#### Log Levels

The `Logger` provides multiple [levels of logging](http://www.thejoyofcode.com/Logging_Levels_and_how_to_use_them.aspx) so you can control what goes into your file:

- `OFF` - turn logging off
- `DEBUG` - diagnostics for troubleshooting
- `INFO` - general information about how the system is configured or major process steps
- `WARN` - situations that can be automatically recovered
- `ERROR` - problems that affect the current operation, but not the overall system
- `FATAL` - events that would force catastrophic system failures 

#### How to use Logger

Add these import statements to your test file:

```
import com.detroitlabs.katalonmobileutil.logging.Logger as Logger
import com.detroitlabs.katalonmobileutil.logging.Logger.LogLevel as LogLevel
```

Initialize the `Logger` with the `LogLevel` for which you want to see output in your file. Setting a particular log level will show all events at that level or higher.

```
Logger.initialize("/tmp/katalon.log", LogLevel.DEBUG)
Logger.debug("This DEBUG message will be logged to the file.")
Logger.info("This INFO message will be logged to the file.")
Logger.warn("This WARN message will be logged to the file.")
Logger.error("This ERROR message will be logged to the file.")
Logger.fatal("This FATAL message will be logged to the file.")
```

```
Logger.initialize("/tmp/katalon.log", LogLevel.INFO)
Logger.debug("This DEBUG message will NOT be logged to the file.")
```

```
Logger.initialize("/tmp/katalon.log", LogLevel.OFF)
Logger.debug("This DEBUG message will NOT be logged to the file.")
Logger.info("This INFO message will NOT be logged to the file.")
Logger.warn("This WARN message will NOT be logged to the file.")
Logger.error("This ERROR message will NOT be logged to the file.")
Logger.fatal("This FATAL message will NOT be logged to the file.")
```

## Resources

[Katalon API Documentation](https://api-docs.katalon.com/studio/v5.0/api/index.html)

[Appium API Documentation](http://appium.io/docs/en/about-appium/api/)

[Selenium API Documentation](https://seleniumhq.github.io/selenium/docs/api/java/)

## Contact
[Chris Trevarthen](mailto:chris.trevarthen+gh@detroitlabs.com) c/o [Detroit Labs](http://detroitlabs.com)

## License

katalon-mobile-util is available under the Apache License, Version 2.0. See the LICENSE file for more info.

