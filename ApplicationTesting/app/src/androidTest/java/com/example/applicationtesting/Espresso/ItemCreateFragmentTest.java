package com.example.applicationtesting.Espresso;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.example.applicationtesting.R;
import com.example.applicationtesting.Operations.MainActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.util.regex.Pattern.matches;

public class ItemCreateFragmentTest
{
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCreateItem()
    {

        onView(withId(R.id.editTextName)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextDescription)).check(matches(isDisplayed()));

        onView(withId(R.id.editTextName)).perform(typeText("Test Item"));

        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));
    }
}