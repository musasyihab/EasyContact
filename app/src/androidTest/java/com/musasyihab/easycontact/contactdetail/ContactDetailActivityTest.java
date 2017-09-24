package com.musasyihab.easycontact.contactdetail;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.contactlist.ContactListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

/**
 * Created by musasyihab on 9/25/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactDetailActivityTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mActivityTestRule = new ActivityTestRule<>(ContactListActivity.class);

    @Test
    public void layoutExist() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction contactListView = onView(
                allOf(withId(R.id.contact_list_view),
                        isDisplayed()));

        contactListView.perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction avatarLayout = onView(
                allOf(withId(R.id.contact_detail_avatar),
                        isDisplayed()));
        avatarLayout.check(matches(isDisplayed()));

        ViewInteraction nameLayout = onView(
                allOf(withId(R.id.contact_detail_name),
                        isDisplayed()));
        nameLayout.check(matches(isDisplayed()));


    }
}
