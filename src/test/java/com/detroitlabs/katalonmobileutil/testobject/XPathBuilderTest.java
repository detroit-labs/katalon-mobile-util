package com.detroitlabs.katalonmobileutil.testobject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.detroitlabs.katalonmobileutil.device.Device;

@PrepareForTest(Device.class)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*"})
class XPathBuilderTest {

	public XPathBuilderTest() {}
	
	@BeforeEach
	public void setUp() throws Exception {
	}

	@Test
	@DisplayName("creating an xpath with a single class returns an xpath with a single class")
	public void creatingXPathWithSingleClass_returnsXPathWithClass() {
		PowerMockito.mockStatic(Device.class);
		PowerMockito.when(Device.isIOS()).thenReturn(true);
		
		assertEquals("//*[equals(@type, 'XCUITypeButton')]", XPathBuilder.createXPath("XCUITypeButton"));
	}
	
	@Test
	public void creatingXPathWithMultipleClasses_returnsXPathWithMultipleClasses() {
//		assertEquals(XPathBuilder.createXPath(["TextView", "EditTextView"]), "//[equals(@class, 'XCUITypeButton']");
	}

}
