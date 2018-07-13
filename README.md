# katalonsupport
Support library to make mobile UI testing in Katalon easier.

## Prerequisites

This library requires [Katalon Studio](https://www.katalon.com/) to be installed.

Building from source requires [Apache Maven](https://maven.apache.org/).

## Manual installation

To use this `katalonsupport` library in Katalon Studio tests, it is not required that you build from source.

Place the release artifact jar into your Katalon test project's `/Drivers` directory, or follow the Katalon Studio instructions: [How to import external library into your automation project](https://www.katalon.com/resources-center/tutorials/import-java-library/).

## Building from source

The Katalon Studio jar files are not available via Maven Central and are not packaged with the `katalonsupport` library, so we can set up a local maven repository to contain the required files:

1. Create a `lib` directory which will act as a local Maven `.m2` repository:

```
cd katalonsupport
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
  -Dfile="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core_1.0.0.201805301004.jar" \
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

The resulting `.jar` file will be created in the `katalonsupport` library's `target` directory.

## Installing from source

1. Edit the `katalonsupport` project's `pom.xml` file to set `project.target.katalon.directory` to your Katalon Studio test project's `/Drivers` directory.

2. Follow the steps for "Building from source".

3. Build and install the package:

```
mvn install
```

4. The `katalonsupport` `.jar` file will be placed in your Katalon Studio test project's `/Drivers` directory.

## Usage

This `katalonsupport` library provides convenience functions for interacting with Katalon Studio and Appium features:

### Device

Provides information about the platform of the device on which the tests are running. This allows a single set of tests to perform branching logic between iOS and Android without the need to create separate test suites.

Add this import statement to your test file:

```
import com.detroitlabs.katalonsupport.device.Device
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

Katalon Studio logs output from `println()` to the console, but it is often difficult to parse out your particular statement from the logs already being created by appium and Katalon, with no way to control the detail of output going to those logs. 

Katalon Studio also provides a way to write to the Log Viewer tab using [`KeywordLogger`](http://www.sanspantalones.com/2018/02/14/output-status-messages-and-test-information-by-writing-to-the-log-file-viewer-in-katalon-studio/). However, the logged statements are mixed in with the test results in this case. 

`katalonsupport` provides a cleaner way to log your own events to a file of your choosing. 

#### Log Levels

The `Logger` provides multiple [levels of logging](http://www.thejoyofcode.com/Logging_Levels_and_how_to_use_them.aspx) so you can control what goes into your file:

- `OFF` - turn logging off
- `DEBUG` - diagnostics for troubleshooting
- `INFO` - general information about how the system is configured or major process steps
- `WARN` - situations that can be automatically recovered
- `ERROR` - problems that affect the current operation, but not the overall system
- `FATAL` - events that would force catastrophic system failures 


Add these import statements to your test file:

```
import com.detroitlabs.katalonsupport.logging.Logger as Logger
import com.detroitlabs.katalonsupport.logging.Logger.LogLevel as LogLevel
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


### TestObject Finder

The basic elements of Katalon Studio tests are [TestObjects](https://docs.katalon.com/display/KD/Manage+Test+Object). These objects are stored in the Object Repository. To keep thing consistently organized, `katalonsupport` assumes that the following structure will be used to store `TestObjects`. 


Because iOS and Android `TestObject` properties vary slightly, using "iOS Test" and "Android Test" folders allows `katalonsupport` to dynamically switch between the `TestObjects` based on the test device platform. Store the `TestObject` with the same name for each platform, e.g. `Checkout button` and `katalonsupport` will pick the correct object for the platform.

Add this import statement to your test file:

```
import com.detroitlabs.katalonsupport.testobject.Finder
```

Find a `TestObject` from the Object Repository:

```
TestObject tab = Finder.findTab('My Tab')
TestObject button = Finder.findButton('My Button')
TestObject label = Finder.findLabel('My Label')
TestObject alert = Finder.findAlert('My Alert')
TestObject textField = Finder.findTextField('My Text Field')
TestObject genericObject = Finder.findGeneric('My Uncategorized Generic Object')
```

### TextField

iOS and Android text fields are represented differently within the structure of a screen. iOS text fields are often directly accessible as `XCUIElementTypeTextField` with an `accessibility id` or (`name` as it is referred to in Katalon Studio `TestObjects`). We can interact with these fields using Katalon's `MobileBuiltInKeywords` class:

```
MobileBuiltInKeywords.setText(textFieldObject, 'Text to set', timeout)
```

Android text fields are sometimes auto-wrapped in an `android.widget.RelativeLayout`, where the `RelativeLayout` gets the `resource-id` reference, not the text field itself. Katalon's `MobileBuiltInKeywords.setText()` doesn't work with `RelativeLayout` objects.

`katalonsupport` provides a wrapper to interact with text fields in both iOS or Android; the implementation is determined by the test device platform.

Add this import statement to your test file:

```
import com.detroitlabs.katalonsupport.textfield.TextField
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

`katalonsupport` provides a wrapper to scroll lists using the [Appium TouchAction](http://appium.io/docs/en/writing-running-appium/touch-actions/).


Add this import statement to your test file:

```
import com.detroitlabs.katalonsupport.touch.Scroll
```

Scroll element collection with the `accessibility id` (for iOS) or `resource-id` (for Android) until you get to the given text:

 ```
 Scroll.scrollListToElementWithText('states', 'Michigan')
 ```
 
 Once scrolling is complete, you can interact with the element:
 
 ```
 Mobile.tap(Finder.findLabel('Michigan'), timeout)
 ```

## Resources

[Katalon API Documentation](https://api-docs.katalon.com/studio/v5.0/api/index.html)

[Appium API Documentation](http://appium.io/docs/en/about-appium/api/)

[Selenium API Documentation](https://seleniumhq.github.io/selenium/docs/api/java/)

## License

katalonsupport is available under the Apache License, Version 2.0. See the LICENSE file for more info.

