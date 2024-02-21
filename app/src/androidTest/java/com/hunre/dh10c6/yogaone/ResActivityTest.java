package com.hunre.dh10c6.yogaone;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.hunre.dh10c6.yogaone.Login_Res.LoginActivity;
import com.hunre.dh10c6.yogaone.Login_Res.ResActivity;
import com.hunre.dh10c6.yogaone.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ResActivityTest {

    @Rule
    public ActivityTestRule<ResActivity> activityRule = new ActivityTestRule<>(ResActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testRegisterButton() {
        // Click the register button
        Espresso.onView(ViewMatchers.withId(R.id.gotoLogin)).perform(ViewActions.click());

        // Check if the login button click opens the LoginActivity
        Intents.intended(IntentMatchers.hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testCheckFieldValidation() {
        // Check Full Name field
        testFieldValidation(R.id.registerName, "", "Full name cannot be empty");
        testFieldValidation(R.id.registerName, "TooLongFullNameTooLongFullNameTooLongFullName", "Full name cannot exceed 100 characters");

        // Check Email field
        testFieldValidation(R.id.registerEmail, "", "Field cannot be empty");
        testFieldValidation(R.id.registerEmail, "invalidEmail", "Invalid email format");

        // Check Password field
        testFieldValidation(R.id.registerPassword, "", "Field cannot be empty");
        testFieldValidation(R.id.registerPassword, "invalidPassword", "Password must be at least 6 characters with one uppercase letter and one special character");

        // Check Phone field
        testFieldValidation(R.id.registerPhone, "", "Field cannot be empty");

        // Check Confirm Password field
        testFieldValidation(R.id.registerConfirmPassword, "", "Field cannot be empty");
        testFieldValidation(R.id.registerConfirmPassword, "password1", "Passwords do not match");
    }

    private void testFieldValidation(int fieldId, String value, String errorMessage) {
        Espresso.onView(withId(fieldId)).perform(ViewActions.replaceText(value));
        Espresso.onView(withId(R.id.registerbtn)).perform(ViewActions.click());
        Espresso.onView(withId(fieldId)).check(matches(hasErrorText(errorMessage)));
    }

    private void testEmptyFieldValidation(int fieldId, String errorMessage) {
        Espresso.onView(withId(fieldId)).perform(ViewActions.clearText());
        Espresso.onView(withId(R.id.registerbtn)).perform(ViewActions.click());
        Espresso.onView(withId(fieldId)).check(matches(hasErrorText(errorMessage)));
    }

    private void testInvalidEmailValidation(int fieldId, String errorMessage) {
        Espresso.onView(withId(fieldId)).perform(ViewActions.replaceText("invalidEmail"));
        Espresso.onView(withId(R.id.registerbtn)).perform(ViewActions.click());
        Espresso.onView(withId(fieldId)).check(matches(hasErrorText(errorMessage)));
    }

    private void testInvalidPasswordValidation(int fieldId, String errorMessage) {
        Espresso.onView(withId(fieldId)).perform(ViewActions.replaceText("invalidPassword"));
        Espresso.onView(withId(R.id.registerbtn)).perform(ViewActions.click());
        Espresso.onView(withId(fieldId)).check(matches(hasErrorText(errorMessage)));
    }

    private void testPasswordConfirmationValidation(String errorMessage) {
        // Set different passwords
        Espresso.onView(withId(R.id.registerPassword)).perform(ViewActions.replaceText("password1"));
        Espresso.onView(withId(R.id.registerConfirmPassword)).perform(ViewActions.replaceText("password2"));
        Espresso.onView(withId(R.id.registerbtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.registerConfirmPassword)).check(matches(hasErrorText(errorMessage)));
    }
}
