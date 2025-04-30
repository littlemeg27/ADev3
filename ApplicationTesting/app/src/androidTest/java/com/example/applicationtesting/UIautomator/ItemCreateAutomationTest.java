package com.example.applicationtesting.UIautomator;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.UiObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ItemCreateAutomationTest
{
    private UiDevice device;

    @Before
    public void setUp() throws UiObjectNotFoundException
    {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject createButton = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/buttonCreate"));
        createButton.click();
    }

    @Test
    public void testCreateItem() throws UiObjectNotFoundException
    {
        UiObject nameField = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/editTextName"));
        UiObject descField = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/editTextDescription"));
        UiObject saveButton = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/buttonSave"));

        nameField.setText("Test Item");
        descField.setText("Test Description");
        saveButton.click();

        UiObject listView = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/textViewItems"));
        assertTrue(listView.exists());
    }
}
