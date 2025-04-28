package com.example.applicationtesting.UIautomator;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ItemCreateAutomationTest {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Navigate to Create Fragment
        UiObject createButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonCreate"));
        createButton.click();
    }

    @Test
    public void testCreateItem() {
        UiObject nameField = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/editTextName"));
        UiObject descField = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/editTextDescription"));
        UiObject saveButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonSave"));

        nameField.setText("Test Item");
        descField.setText("Test Description");
        saveButton.click();

        UiObject listView = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/textViewItems"));
        assertTrue(listView.exists());
    }
}