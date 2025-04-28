package com.example.applicationtesting.Espresso;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.crudapp.R;
import com.example.crudapp.ui.MainActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ItemCreateFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCreateItem() {
        // Navigate to Create Fragment
        onView(withId(R.id.buttonCreate)).perform(click());

        // Verify UI elements
        onView(withId(R.id.editTextName)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonSave)).check(matches(isDisplayed()));

        // Perform actions
        onView(withId(R.id.editTextName)).perform(typeText("Test Item"));
        onView(withId(R.id.editTextDescription)).perform(typeText("Test Description"));
        onView(withId(R.id.buttonSave)).perform(click());

        // Verify return to list
        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));
    }
}