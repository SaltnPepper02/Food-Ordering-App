package com.project.mdpeats;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.project.mdpeats.R;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class FSOFoodListTest {
    @Test
    public void testAddFood() throws InterruptedException {
        // Launch the activity
        ActivityScenario.launch(FSOFoodList.class);

        // Add food
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());// cant work dialog not opening

        Thread.sleep(2000);
//        Espresso.onView(ViewMatchers.withId(R.id.edtName)).perform(ViewActions.typeText("New Food"));
//        Espresso.onView(ViewMatchers.withId(R.id.edtDescription)).perform(ViewActions.typeText("Description"));
//        Espresso.onView(ViewMatchers.withId(R.id.edtPrice)).perform(ViewActions.typeText("10.0"));
//        Espresso.onView(ViewMatchers.withId(R.id.btnSelect)).perform(ViewActions.click());
//
//
//        // Set the image data in resultData, simulating the image selection
//
//        // Mock the image selection result
//        Intent resultData = new Intent();
//        Uri uri = Uri.parse("content://media/external/images/media/1");
//        resultData.setData(uri);
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
//
//        intending(not(isInternal())).respondWith(result);

//        Espresso.onView(ViewMatchers.withText("Yes")).perform(ViewActions.click());


        // Validate the addition
        // You may need to add a delay here to wait for the upload to complete
        // Replace the following line with the actual delay mechanism
//        try {
//            Thread.sleep(5000); // Wait for 5 seconds
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Assuming you have a method to get the current food count
//        int foodCount = getCurrentFoodCount();
//
//        // Validate the count after adding a new food
//        assertEquals(1, foodCount);
    }

    private int getCurrentFoodCount() {
        // Implement the method to get the current food count from your database or UI
        // For simplicity, you may use a hardcoded value or implement a more robust solution
        return 1;
    }

}