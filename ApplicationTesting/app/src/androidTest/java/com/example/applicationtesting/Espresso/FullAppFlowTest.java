package com.example.applicationtesting.Espresso;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.applicationtesting.Operations.MainActivity;
import com.example.applicationtesting.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FullAppFlowTest
{
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFullAppFlow()
    {

        onView(withId(R.id.action_create))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.editTextName))
                .perform(typeText("Test Item"), closeSoftKeyboard());
        onView(withId(R.id.editTextDescription))
                .perform(typeText("Test Description"), closeSoftKeyboard());
        onView(withId(R.id.action_save))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.textViewItems))
                .check(matches(isDisplayed()));

        onView(withId(R.id.action_delete))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.editTextId))
                .perform(typeText("any_id"), closeSoftKeyboard());
        onView(withId(R.id.action_delete))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.textViewItems))
                .check(matches(isDisplayed()));
    }
}