package com.example.mealfinder;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AndroidTest {

    @Rule
    public ActivityScenarioRule<FirstTimeUsers> activityScenarioRule =
            new ActivityScenarioRule<>(FirstTimeUsers.class);

    @Test
    public void testFirstTimeUsers() {
        onView(withId(R.id.macrobiotic_card_view)).perform(click());
        onView(withId(R.id.vegan_card_view)).perform(click());

        onView(withId(R.id.macrobiotic_card_view)).perform(click());

    }

}