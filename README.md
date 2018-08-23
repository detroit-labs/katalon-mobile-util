# katalon-mobile-util
[Katalon Studio](https://www.katalon.com/) is an IDE that provides a unified way to test UI for mobile and web. 

**katalon-mobile-util** is a library of utilities to make mobile UI testing in Katalon Studio easier.

## Installation

To use this **katalon-mobile-util** library in Katalon Studio tests, it is not required that you build from source.

Place the [release artifact jar](https://github.com/detroit-labs/katalon-mobile-util/releases/download/1.2.0/katalon-mobile-util-1.2.0.jar) into your Katalon test project's `/Drivers` directory, or follow the Katalon Studio instructions: [How to import external library into your automation project](https://www.katalon.com/resources-center/tutorials/import-java-library/).

## Building from source

Although not required, you may build the **katalon-mobile-util** library from source code. The resulting `.jar` will be added to your Katalon Studio test project.

### Prerequisites

This library requires [Katalon Studio](https://www.katalon.com/) to be installed.

Building from source requires [Apache Maven](https://maven.apache.org/).

### Building

The Katalon Studio jar files are not available via Maven Central and are not packaged with the **katalon-mobile-util** library, so we can set up a local maven repository to contain the required files:

1. Create a `lib` directory which will act as a local Maven `.m2` repository:

```
cd katalon-mobile-util
mkdir lib
```

2. Copy the jar files from Katalon Studio to the `lib` directory:

```
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core_1.0.0.201805301004.jar" \
  -DgroupId=com.kms.katalon \
  -DartifactId=core \
  -Dversion=1.0.0.201805301004 \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib
```

```
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.mobile_1.0.0.201805301004.jar" \
  -DgroupId=com.kms.katalon.core \
  -DartifactId=mobile \
  -Dversion=1.0.0.201805301004 \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib
```

4. Build the package:

```
mvn package
```

The resulting `.jar` file will be created in the **katalon-mobile-util** library's `target` directory.

## Installing from source

1. Edit the **katalon-mobile-util** project's `pom.xml` file to set `project.target.katalon.directory` to your Katalon Studio test project's `/Drivers` directory.

2. Follow the steps for "Building from source".

3. Build and install the package:

```
mvn install
```

4. The **katalon-mobile-util** `.jar` file will be placed in your Katalon Studio test project's `/Drivers` directory.

## Usage

This **katalon-mobile-util** library provides convenience functions for interacting with Katalon Studio and Appium features:

### Device

Provides information about the platform of the device on which the tests are running. This allows a single set of tests to perform branching logic between iOS and Android without the need to create separate test suites.

#### How to use Device

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.device.Device
```

Start the test application on the device, using the device platform to determine which application file to load - will reset the simulator.

```
String androidKit = '~/Downloads/mobile-beta.apk'
String iosKit = '~/Downloads/mobile-beta.app'
Device.startApp(iosKit, androidKit, true)
```

OR, start the test application on the device, using the device platform to determine which application file to load - will NOT reset the simulator between each run.

```
Device.startApp(iosKit, androidKit)
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


### TestObjects

#### Organization

The basic elements of Katalon Studio tests are [TestObjects](https://docs.katalon.com/display/KD/Manage+Test+Object). These objects are stored in the Object Repository. To keep thing consistently organized, **katalon-mobile-util** assumes that the following structure will be used to store `TestObjects`:

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

#### Finding a Label from a List by Index

In iOS, we can't specify an `accessibility id` for a `UITableView`, but we can use the `accessibility id` assigned to similar labels in the table view to find a specific element.

To find a specific label from a collection of similar labels on the screen, first create a new Label TestObject in the Object Repository. The new Label should have properties for the `type` and `name` for iOS (or `class` and `resource-id` for Android). You should leave off the `label` or `text` properties in order to keep the Label generic to represent the collection of 

Then provide an index for which element in the list you want to find (indexes start at 0):

```
int index = 3 // first element in the list is at index 0
TestObject labelAtIndex = Finder.findLabelAtIndex('Generic label element', index) 
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
import com.detroitlabs.katalonmobileutil.textfield.TextField
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


### Scrolling

Katalon Studio provides a method for [scrolling](https://docs.katalon.com/display/KD/%5BMobile%5D+Scroll+To+Text) a list until particular text is visible on the screen. Unfortunately, this function has proven problematic and results in erratic scrolling.

**katalon-mobile-util** provides a wrapper to scroll lists using the [Appium TouchAction](http://appium.io/docs/en/writing-running-appium/touch-actions/).

#### How to use Scrolling

Add this import statement to your test file:

```
import com.detroitlabs.katalonmobileutil.touch.Scroll
```

Scroll list of all `XCUIElementTypeStaticText` (for iOS) or `*TextView` (for Android) elements until you get to the given text:

 ```
 Scroll.scrollListToElementWithText('Michigan')
 ```

Scroll specific list of elements with the `accessibility id` (for iOS) or `resource-id` (for Android) until you get to the given text. In this case, all of the elements in the collection should have the same ids, e.g. `state_label`:

 ```
 Scroll.scrollListToElementWithText('state_label', 'Michigan')
 ```
 
 Once scrolling is complete, you can interact with the element, assuming you already have a TestObject named "Michigan label".
 
 ```
 Mobile.tap(Finder.findLabel('Michigan label'), timeout)
 ```

## Resources

[Katalon API Documentation](https://api-docs.katalon.com/studio/v5.0/api/index.html)

[Appium API Documentation](http://appium.io/docs/en/about-appium/api/)

[Selenium API Documentation](https://seleniumhq.github.io/selenium/docs/api/java/)

## Contact
[Chris Trevarthen](mailto:chris.trevarthen+gh@detroitlabs.com) c/o [Detroit Labs](http://detroitlabs.com)

## License

katalon-mobile-util is available under the Apache License, Version 2.0. See the LICENSE file for more info.

