package com.musasyihab.easycontact.contactlist;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.musasyihab.easycontact.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactListActivityTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mActivityTestRule = new ActivityTestRule<>(ContactListActivity.class);

    @Test
    public void layoutExist() {

        ViewInteraction loadingView = onView(
                allOf(withId(R.id.contact_list_loading),
                        isDisplayed()));
        loadingView.check(matches(isDisplayed()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction contactListView = onView(
                allOf(withId(R.id.contact_list_view),
                    isDisplayed()));
        contactListView.check(matches(isDisplayed()));

        ViewInteraction fabButton = onView(
                allOf(withId(R.id.contact_list_add_fab),
                        isDisplayed()));
        fabButton.check(matches(isDisplayed()));


    }
}
