package com.example.applicationtesting.UIautomator;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ItemDeleteAutomationTest {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Navigate to Delete Fragment
        UiObject deleteButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonDelete"));
        deleteButton.click();
    }

    @Test
    public void testDeleteItem() {
        UiObject idField = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/editTextId"));
        UiObject deleteButton = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/buttonDelete"));

        idField.setText("1");
        deleteButton.click();

        UiObject listView = device.findObject(new UiSelector().resourceId("com.example.crudapp:id/textViewItems"));
        assertTrue(listView.exists());
    }
}
