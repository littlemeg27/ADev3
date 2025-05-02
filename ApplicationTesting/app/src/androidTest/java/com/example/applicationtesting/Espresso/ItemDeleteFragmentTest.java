package com.example.applicationtesting.Espresso;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.applicationtesting.Operations.MainActivity;
import com.example.applicationtesting.R;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

// Brenna Pavlinchak
// AD3 - C202504
// ItemDeleteFragmentTest

public class ItemDeleteFragmentTest
{
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testDeleteItem()
    {

        onView(withId(R.id.editTextId)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextId)).perform(typeText("1"));

        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));
    }
}