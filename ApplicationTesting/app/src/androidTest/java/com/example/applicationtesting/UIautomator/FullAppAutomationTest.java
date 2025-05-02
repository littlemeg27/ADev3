package com.example.applicationtesting.UIautomator;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

// Brenna Pavlinchak
// AD3 - C202504
// FullAppAutomationTest

public class FullAppAutomationTest
{
    private UiDevice device;
    private static final int TIMEOUT = 3000;

    @Before
    public void setUp()
    {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.example.applicationtesting");

        if (intent != null)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            device.waitForIdle(1000);
        }
    }

    @Test
    public void testFullAppFlow() throws UiObjectNotFoundException
    {
        UiObject createButton = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/action_create")
                .textContains("Create"));

        if (!createButton.waitForExists(TIMEOUT))
        {
            throw new AssertionError("Create button not found");
        }
        createButton.click();
        device.waitForIdle(1000);

        UiObject nameField = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/editTextName"));
        UiObject descField = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/editTextDescription"));
        UiObject saveButton = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/action_save")
                .textContains("Save"));

        if (!nameField.waitForExists(TIMEOUT) || !descField.waitForExists(TIMEOUT))
        {
            throw new AssertionError("Create form fields not found");
        }

        nameField.setText("Test Item");
        descField.setText("Test Description");

        if (!saveButton.waitForExists(TIMEOUT))
        {
            throw new AssertionError("Save button not found");
        }
        saveButton.click();
        device.waitForIdle(1000);

        UiObject deleteButton = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/action_delete")
                .textContains("Delete"));

        if (!deleteButton.waitForExists(TIMEOUT))
        {
            throw new AssertionError("Delete button not found");
        }
        deleteButton.click();
        device.waitForIdle(1000);

        UiObject idField = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/editTextId"));

        if (!idField.waitForExists(TIMEOUT))
        {
            throw new AssertionError("ID field not found");
        }
        idField.setText("any_id");

        if (!deleteButton.waitForExists(TIMEOUT))
        {
            throw new AssertionError("Delete button not found");
        }

        deleteButton.click();
        device.waitForIdle(1000);

        UiObject listView = device.findObject(new UiSelector()
                .resourceId("com.example.applicationtesting:id/textViewItems"));

        if (!listView.waitForExists(TIMEOUT))
        {
            throw new AssertionError("List view not found");
        }
        assertTrue("List view not displayed", listView.exists());
    }
}