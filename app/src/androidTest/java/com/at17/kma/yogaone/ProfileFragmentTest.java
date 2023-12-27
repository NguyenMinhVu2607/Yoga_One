package com.at17.kma.yogaone;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.at17.kma.yogaone.R;
import com.at17.kma.yogaone.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule =
            new ActivityScenarioRule<>(SplashActivity.class);

    @Before
    public void setUp() {
        FirebaseAuth.getInstance().signOut();
        activityRule.getScenario().onActivity(activity -> {
            activity.startActivity(new Intent(activity, SplashActivity.class));
        });
    }

    @Test
    public void testProfileFragmentUI() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Sign in with a test user
            auth.signInWithEmailAndPassword("test@example.com", "testpassword");
        }

        // Verify that essential UI components are displayed
        onView(withId(R.id.emailTextViewStudent)).check(matches(isDisplayed()));
        onView(withId(R.id.usernameTextViewStudent)).check(matches(isDisplayed()));
        onView(withId(R.id.logout)).check(matches(isDisplayed()));
        onView(withId(R.id.showAddressesButton)).check(matches(isDisplayed()));

        // Check email verification status
        onView(withId(R.id.sendCode)).check(matches(isDisplayed()));
        onView(withId(R.id.infoVerify)).check(matches(isDisplayed()));



        // Click on the logout button
        onView(withId(R.id.logout)).perform(click());
    }
}
