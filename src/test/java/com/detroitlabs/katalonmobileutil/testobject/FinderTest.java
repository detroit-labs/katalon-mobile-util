package com.detroitlabs.katalonmobileutil.testobject;

import com.detroitlabs.katalonmobileutil.device.Device;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.testobject.ObjectRepository;
import com.kms.katalon.core.testobject.TestObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Device.class, ObjectRepository.class, KeywordLogger.class})
public class FinderTest {

    // Treat as @Before/@BeforeAll
    public FinderTest() {
        PowerMockito.mockStatic(Device.class);
        PowerMockito.mockStatic(ObjectRepository.class);
        PowerMockito.mockStatic(KeywordLogger.class);
    }

    @Before
    public void beforeEach() {
        PowerMockito.when(Device.isIOS()).thenReturn(true);
    }

    @Test
    @DisplayName("finding a label test object calls findTestObject with the label name")
    public void findingALabelTestObject_callsFindTestObjectWithLabelPath() {
        PowerMockito.when(ObjectRepository.findTestObject("iOS Objects/Labels/Test Label")).thenReturn(new TestObject());
        TestObject label = Finder.findLabel("Test Label");
        assertNotNull(label);
    }

    @Test
    @DisplayName("finding a generic test object calls findTestObject with the top level platform-specific directory")
    public void findingAGenericTestObject_callsFindTestObjectWithGenericPath() {
        PowerMockito.when(ObjectRepository.findTestObject("iOS Objects/Test Generic")).thenReturn(new TestObject());
        TestObject label = Finder.findGeneric("Test Generic");
        assertNotNull(label);
    }

}
