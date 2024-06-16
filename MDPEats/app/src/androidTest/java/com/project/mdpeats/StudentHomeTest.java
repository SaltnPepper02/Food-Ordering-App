package com.project.mdpeats;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.project.mdpeats.MainActivity;
import com.project.mdpeats.R;
import com.project.mdpeats.StudentHome;
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import junit.framework.TestCase;
@RunWith(AndroidJUnit4.class)
public class StudentHomeTest{
    @Rule
    public ActivityTestRule<StudentHome> activityRule = new ActivityTestRule<>(StudentHome.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void testNavigationView() {
//        // Wait for UI to settle down
//        BaristaSleepInteractions.sleep(1000);

        // Open the navigation drawer using the overflow menu
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Check if the view with text "Cart" is visible
        Espresso.onView(withText("Cart")).check(matches(isDisplayed()));

        // Perform a click action on the view with text "Cart"
        Espresso.onView(withText("Cart")).perform(ViewActions.click());

        // Use ActivityTestRule to launch the Cart activity
        ActivityTestRule<Cart> activityRule = new ActivityTestRule<>(Cart.class);
        activityRule.launchActivity(null);

        // Check if a specific view in the Cart activity is displayed (replace R.id.some_view with the actual ID of a view in the Cart activity)
        Espresso.onView(withId(R.id.cartList)).check(matches(isDisplayed()));

    }


    @Test
    public void testFabButtonClick() {
        // Click on the FAB button
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());

        // Check if the correct activity is launched
        Intents.intended(IntentMatchers.hasComponent(Cart.class.getName()));
    }

    // Add more tests for other UI interactions and behaviors if needed

    @After
    public void tearDown() {
        Intents.release();
    }



}