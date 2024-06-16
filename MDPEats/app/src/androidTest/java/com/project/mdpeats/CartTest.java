package com.project.mdpeats;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.project.mdpeats.Cart;
import com.project.mdpeats.Database.Database;
import com.project.mdpeats.Models.CartOrder;
import com.project.mdpeats.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.List;

public class CartTest {

    @Rule
    public ActivityTestRule<Cart> activityRule = new ActivityTestRule<>(Cart.class);

    @Before
    public void setUp() {
        // You can perform setup tasks here before each test
    }


    @Test
    public void checkCartOpen() {
        // Check if the cart is not empty
        onView(ViewMatchers.withId(R.id.cartList)).check(matches(isDisplayed()));


    }
    @Test
    public void checkCartNotEmpty() {
        // Check if the cart is not empty
        onView(withId(R.id.cartList)).check(matches(hasMinimumChildCount(1)));

    }

    @Test
    public void placeOrderButton_Click() {
        // Check if there are items in the cart
        List<CartOrder> cart = new Database(InstrumentationRegistry.getInstrumentation().getTargetContext()).getCarts();

        if (cart.size() > 0) {
            // Click on the "Place Order" button
            onView(withId(R.id.btnCart)).perform(click());

            // Check if the confirmation dialog is displayed
            onView(withText("Confirm Order?"))
                    .inRoot(isDialog()) // Assumes it's a dialog
                    .check(matches(isDisplayed()));

            // Click on the "YES" button in the confirmation dialog
            onView(withText("YES")).perform(click());

            // Check if the "Order has been placed" toast message is displayed
            onView(withText("Order has been placed"))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
        } else {
            System.out.println("Cart is empty. Cannot place an order.");
        }
    }

    // Add more test cases as needed

    @After
    public void tearDown() {
        // Clean up or reset any test-specific conditions
    }
}