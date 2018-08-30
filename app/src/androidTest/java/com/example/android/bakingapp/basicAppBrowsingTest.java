package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class basicAppBrowsingTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    //basic app browsing test case
    @Test
    public void simpleRecipeBrowsing() {

        //select 3rd Recipe from recipe list
        onView(withId(R.id.select_recipe_Recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //select 3 Step from recipe list
        onView(withId(R.id.select_step_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //iterate through steps by pressing next step button
        for (int i = 0; i < 4; i++)
            onView(withId(R.id.next_button)).perform(click());

        //iterate back through steps by pressing previous step button
        for (int i = 0; i < 4; i++) {
            onView(withId(R.id.previous_button)).perform(click());
            //wait 10 seconds for the video to load
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //make sure to install widget to notice the changes happen in this test
    @Test
    public void setWidget() {
        //select 3rd Recipe from recipe list
        onView(withId(R.id.select_recipe_Recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        //click add widget button to set the widget with this recipe
        onView(withText(R.string.add_to_widget_button)).perform(click());
    }

    @After
    public void unRegisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
