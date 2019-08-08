package com.detroitlabs.katalonmobileutil.testobject;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.detroitlabs.katalonmobileutil.device.Device;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Device.class)
class XPathBuilderTest {
	
	// Treat as @Before/@BeforeAll
	public XPathBuilderTest() {
		PowerMockito.mockStatic(Device.class);
	}
	
	@Before
	public void beforeEach() throws Exception {
		PowerMockito.when(Device.isIOS()).thenReturn(true);
	}

	@Test
	@DisplayName("creating an xpath with a single class returns an xpath with a single class")
	public void creatingXPathWithSingleClass_returnsXPathWithClass() {

		// iOS
		assertEquals("//*[@type='XCUITypeButton']", XPathBuilder.createXPath("XCUITypeButton"));

		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		assertEquals("//*[contains(@class, 'TextView')]", XPathBuilder.createXPath("TextView"));
	}

	@Test
	@DisplayName("creating an xpath with multiple classes returns an xpath with multiple classes")
	public void creatingXPathWithMultipleClasses_returnsXPathWithMultipleClasses() {

		// iOS
		List<String> types = Arrays.asList("XCUITypeButton", "XCUITypeCheckbox");
		assertEquals("//*[(@type='XCUITypeButton' or @type='XCUITypeCheckbox')]",
				XPathBuilder.createXPath(types));

		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		types = Arrays.asList("TextView", "CheckBox");
		assertEquals("//*[(contains(@class, 'TextView') or contains(@class, 'CheckBox'))]",
				XPathBuilder.createXPath(types));
	}

	@Test
	@DisplayName("when xpath already includes properties adding a visible property updates the xpath to include the 'visible' property")
	public void whenXPathAlreadyIncludesProperties_addingVisibleProperty_updatesXPathWithVisible() {

		String xpath = "//*[@type='XCUITypeButton']";
		assertEquals("//*[@type='XCUITypeButton' and @visible='true']", XPathBuilder.addVisible(xpath));

	}
	
	@Test
	@DisplayName("when xpath does not include properties adding a visible property updates the xpath to include the 'visible' property")
	public void whenXPathDoesNotIncludeProperties_addingVisibleProperty_updatesXPathWithVisible() {

		String xpath = "//XCUITypeButton";
		assertEquals("//XCUITypeButton[@visible='true']", XPathBuilder.addVisible(xpath));

	}
	
	@Test
	@DisplayName("when xpath already includes properties adding a label updates the xpath to include a 'label' property")
	public void whenXPathAlreadyIncludesProperties_addingLabelProperty_updatesXPathWithLabel() {

		// iOS
		String xpath = "//*[@type='XCUITypeButton']";
		assertEquals("//*[@type='XCUITypeButton' and starts-with(normalize-space(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.addLabel(xpath, "My Label"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		xpath = "//*[contains(@class, 'TextView')]";
		assertEquals("//*[contains(@class, 'TextView') and starts-with(normalize-space(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.addLabel(xpath, "My Label"));
	}
	
	@Test
	@DisplayName("when xpath does not already include properties adding a label updates the xpath to include a 'label' property")
	public void whenXPathDoesNotIncludeProperties_addingLabelProperty_updatesXPathWithLabel() {

		// iOS
		String xpath = "//XCUITypeButton";
		assertEquals("//XCUITypeButton[starts-with(normalize-space(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.addLabel(xpath, "My Label"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		xpath = "//TextView";
		assertEquals("//TextView[starts-with(normalize-space(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.addLabel(xpath, "My Label"));
	}
	
	@Test
	@DisplayName("when xpath already includes properties adding a resource id updates the xpath to include a resource id property")
	public void whenXPathAlreadyIncludesProperties_addingResourceIdProperty_updatesXPathWithResourceId() {

		// iOS
		String xpath = "//*[@type='XCUITypeButton']";
		assertEquals("//*[@type='XCUITypeButton' and @name='my_button']", XPathBuilder.addResourceId(xpath, "my_button"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		xpath = "//*[contains(@class, 'TextView')]";
		assertEquals("//*[contains(@class, 'TextView') and contains(@resource-id, 'my_button')]", XPathBuilder.addResourceId(xpath, "my_button"));
	}
	
	@Test
	@DisplayName("creating an xpath for label with resource id creates an xpath with resource id only")
	public void creatingAnXPathForALabelWithResourceId_returnsAnXPathWithALabel() {

		// iOS
		assertEquals("//*[@type='XCUIElementTypeStaticText' and @name='my_label']", XPathBuilder.xpathForLabelWithResourceId("my_label"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		assertEquals("//*[contains(@class, 'TextView') and contains(@resource-id, 'my_label')]", XPathBuilder.xpathForLabelWithResourceId("my_label"));
	}	
	
	@Test
	@DisplayName("creating an xpath for label with resource id and text creates an xpath with resource id and text")
	public void creatingAnXPathForALabel_returnsAnXPathWithALabel() {

		// iOS
		assertEquals("//*[@type='XCUIElementTypeStaticText' and @name='my_label' and starts-with(normalize-space(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.xpathForLabel("my_label", "My Label"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		assertEquals("//*[contains(@class, 'TextView') and contains(@resource-id, 'my_label') and starts-with(normalize-space(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.xpathForLabel("my_label", "My Label"));
	}
	
	@Test
	@DisplayName("creating an xpath for label with text creates an xpath with text")
	public void creatingAnXPathForALabel_returnsAnXPathWithText() {

		// iOS
		assertEquals("//*[@type='XCUIElementTypeStaticText' and starts-with(normalize-space(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.xpathForLabelWithText("My Label"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		assertEquals("//*[contains(@class, 'TextView') and starts-with(normalize-space(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.xpathForLabelWithText("My Label"));
	}
	
	@Test
	@DisplayName("creating an xpath for checkbox with text creates an xpath with text")
	public void creatingAnXPathForACheckbox_returnsAnXPathWithText() {

		// iOS
		assertEquals("//*[@type='XCUIElementTypeStaticText' and starts-with(normalize-space(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.xpathForCheckboxWithText("My Label"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		assertEquals("//*[contains(@class, 'CheckBox') and starts-with(normalize-space(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), \"my label\")]", XPathBuilder.xpathForCheckboxWithText("My Label"));
	}	
	
	@Test
	@DisplayName("adding a child to an xpath creates an xpath with the child appended")
	public void addingAChildToAnXPath_returnsAnXPathWithTheChild() {

		// iOS
		String xpath = "//*[@type='XCUIElementTypeOther']";
		assertEquals(xpath + "/*[@type='XCUIElementTypeStaticText']", XPathBuilder.addChildWithType(xpath, "XCUIElementTypeStaticText"));
		
		// Android
		PowerMockito.when(Device.isIOS()).thenReturn(false);
		xpath = "//*[contains(@class, 'Spinner')]";
		assertEquals(xpath + "/*[contains(@class, 'TextView')]", XPathBuilder.addChildWithType(xpath, "TextView"));
	}	

}
