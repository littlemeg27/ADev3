package com.example.applicationtesting.Espresso;

import static android.os.Build.VERSION_CODES.R;

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
import static java.util.regex.Pattern.matches;

public class FullAppFlowTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFullAppFlow() {
        // Create an item
        onView(withId(R.id.buttonCreate)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Test Item"));
        onView(withId(R.id.editTextDescription)).perform(typeText("Test Description"));
        onView(withId(R.id.buttonSave)).perform(click());

        // Verify item in list
        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));

        // Delete an item
        onView(withId(R.id.buttonDelete)).perform(click());
        onView(withId(R.id.editTextId)).perform(typeText("1"));
        onView(withId(R.id.buttonDelete)).perform(click());

        // Verify back to list
        onView(withId(R.id.textViewItems)).check(matches(isDisplayed()));
    }
}