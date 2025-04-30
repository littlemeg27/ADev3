package com.example.applicationtesting.Espresso;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.Intents;
import com.example.applicationtesting.R;
import com.example.applicationtesting.Operations.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ItemListFragmentTest
{
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testUIElementsDisplayed()
    {
        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToCreateFragment()
    {
        onView(withId(R.id.editTextName)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToDeleteFragment()
    {
        onView(withId(R.id.editTextId)).check(matches(isDisplayed()));
    }
}
