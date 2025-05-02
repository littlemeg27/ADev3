package com.example.applicationtesting.UIautomator;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

// Brenna Pavlinchak
// AD3 - C202504
// ItemDeleteAutomationTest

public class ItemDeleteAutomationTest
{
    private UiDevice device;

    @Before
    public void setUp() throws UiObjectNotFoundException
    {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject deleteButton = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/buttonDelete"));
        deleteButton.click();
    }

    @Test
    public void testDeleteItem() throws UiObjectNotFoundException
    {
        UiObject idField = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/editTextId"));
        UiObject deleteButton = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/buttonDelete"));

        idField.setText("1");
        deleteButton.click();

        UiObject listView = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/textViewItems"));
        assertTrue(listView.exists());
    }
}
