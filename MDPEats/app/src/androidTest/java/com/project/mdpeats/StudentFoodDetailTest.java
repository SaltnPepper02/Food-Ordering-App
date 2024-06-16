package com.project.mdpeats;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.project.mdpeats.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StudentFoodDetailTest {
    @Rule
    public ActivityTestRule<StudentFoodDetail> activityRule =
            new ActivityTestRule<>(StudentFoodDetail.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("FoodId", "01"); // Replace with a valid food ID for testing
        activityRule.launchActivity(intent);
    }

    @Test
    public void checkFoodDetailsDisplay() { //it still runs
        // Check if the food name is displayed
        onView(withId(R.id.food_name)).check(matches(isDisplayed()));

        // Check if the food price is displayed
        onView(withId(R.id.food_price)).check(matches(isDisplayed()));

        // Check if the food description is displayed
        onView(withId(R.id.food_description)).check(matches(isDisplayed()));
    }

    @Test
    public void addToCartButton_Click() {
        // Click on the "Add to Cart" button
        onView(withId(R.id.btnCart)).perform(click());

        // Check if a toast message is displayed after clicking the button
        // Ask
        onView(withText("Added To Cart"))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    // Add more test cases as needed

    @After
    public void tearDown() {
        // Clean up or reset any test-specific conditions
    }

}