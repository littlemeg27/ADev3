package com.example.applicationtesting.UIautomator;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ItemListAutomationTest
{
    private UiDevice device;
    private static final int TIMEOUT = 3000;

    @Before
    public void setUp()
    {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testUIElementsDisplayed()
    {
        try
        {
            device.waitForIdle(1000);

            UiObject listView = device.findObject(new UiSelector()
                    .resourceId("com.example.applicationtesting:id/textViewItems"));
            UiObject createButton = device.findObject(new UiSelector()
                    .resourceIdMatches("com.example.applicationtesting:id/(action_create|buttonCreate)")
                    .textContains("Create"));
            UiObject deleteButton = device.findObject(new UiSelector()
                    .resourceIdMatches("com.example.applicationtesting:id/(action_delete|buttonDelete)")
                    .textContains("Delete"));

            if (!listView.waitForExists(TIMEOUT))
            {
                fail("List view not found");
            }
            if (!createButton.waitForExists(TIMEOUT))
            {
                fail("Create button not found");
            }
            if (!deleteButton.waitForExists(TIMEOUT))
            {
                fail("Delete button not found");
            }

            assertTrue("List view not displayed", listView.exists());
            assertTrue("Create button not displayed", createButton.exists());
            assertTrue("Delete button not displayed", deleteButton.exists());
        }
        catch (Exception e)
        {
            fail("UI element not found: " + e.getMessage());
        }
    }

    @Test
    public void testNavigateToCreateFragment() throws UiObjectNotFoundException
    {
        device.waitForIdle(1000);

        UiObject createButton = device.findObject(new UiSelector()
                .resourceIdMatches("com.example.applicationtesting:id/(action_create|buttonCreate)")
                .textContains("Create"));

        if (!createButton.waitForExists(TIMEOUT))
        {
            fail("Create button not found");
        }

        createButton.click();
        device.waitForIdle(1000);

        UiObject nameField = device.findObject(new UiSelector().resourceId("com.example.applicationtesting:id/editTextName"));

        if (!nameField.waitForExists(TIMEOUT))
        {
            fail("Name field not found after navigation");
        }
        assertTrue("Name field not displayed", nameField.exists());
    }
}