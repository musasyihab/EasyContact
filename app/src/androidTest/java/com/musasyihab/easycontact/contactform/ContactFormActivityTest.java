package com.musasyihab.easycontact.contactform;

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
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by musasyihab on 9/25/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactFormActivityTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mActivityTestRule = new ActivityTestRule<>(ContactListActivity.class);

    @Test
    public void layoutExist() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction fabButton = onView(
                allOf(withId(R.id.contact_list_add_fab),
                        isDisplayed()));

        fabButton.perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction avatarBtnLayout = onView(
                allOf(withId(R.id.contact_form_avatar_btn),
                        isDisplayed()));
        avatarBtnLayout.check(matches(isDisplayed()));

        ViewInteraction firstNameInputLayout = onView(
                allOf(withId(R.id.contact_form_first_name_input),
                        isDisplayed()));
        firstNameInputLayout.check(matches(isDisplayed()));

        ViewInteraction lastNameInputLayout = onView(
                allOf(withId(R.id.contact_form_last_name_input),
                        isDisplayed()));
        lastNameInputLayout.check(matches(isDisplayed()));

        ViewInteraction emailInputLayout = onView(
                allOf(withId(R.id.contact_form_email_input),
                        isDisplayed()));
        emailInputLayout.check(matches(isDisplayed()));

        ViewInteraction phoneInputLayout = onView(
                allOf(withId(R.id.contact_form_phone_input),
                        isDisplayed()));
        phoneInputLayout.check(matches(isDisplayed()));

        ViewInteraction saveMenuView = onView(
                allOf(withId(R.id.action_contact_save), withContentDescription("Save"), isDisplayed()));
        saveMenuView.check(matches(isDisplayed()));


    }
}
