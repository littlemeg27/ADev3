package com.example.applicationtesting.UIautomator;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ItemListAutomationTest {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testUIElementsDisplayed() {
        UiObject listView = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/textViewItems"));
        UiObject createButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonCreate"));
        UiObject deleteButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonDelete"));

        assertTrue(listView.exists());
        assertTrue(createButton.exists());
        assertTrue(deleteButton.exists());
    }

    @Test
    public void testNavigateToCreateFragment() {
        UiObject createButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonCreate"));
        createButton.click();
        UiObject nameField = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/editTextName"));
        assertTrue(nameField.exists());
    }
}
