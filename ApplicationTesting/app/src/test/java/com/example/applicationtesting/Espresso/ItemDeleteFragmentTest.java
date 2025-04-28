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

public class ItemDeleteFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testDeleteItem() {
        // Navigate to Delete Fragment
        onView(withId(R.id.buttonDelete)).perform(click());

        // Verify UI elements
        onView(withId(R.id.editTextId)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonDelete)).check(matches(isDisplayed()));

        // Perform actions
        onView(withId(R.id.editTextId)).perform(typeText("1"));
        onView(withId(R.id.buttonDelete)).perform(click());

        // Verify return to list
        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));
    }
}
