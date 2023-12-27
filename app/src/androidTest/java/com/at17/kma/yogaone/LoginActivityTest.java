package com.at17.kma.yogaone;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.at17.kma.yogaone.Login_Res.LoginActivity;
import com.at17.kma.yogaone.MainActivity;
import com.at17.kma.yogaone.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testLoginFunctionality() {
        // Nhập dữ liệu bất kỳ và nhấp nút đăng nhập
        Espresso.onView(withId(R.id.loginEmail)).perform(ViewActions.replaceText("any_email@example.com"));
        Espresso.onView(withId(R.id.loginPassword)).perform(ViewActions.replaceText("any_password"));
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        // Kiểm tra xem màn hình MainActivity có được mở không
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }
    @Test
    public void testInvalidLogin() {
        // Nhập dữ liệu không hợp lệ và nhấp nút đăng nhập
        Espresso.onView(withId(R.id.loginEmail)).perform(ViewActions.replaceText("invalid_email"));
        Espresso.onView(withId(R.id.loginPassword)).perform(ViewActions.replaceText("invalid_password"));
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        // Kiểm tra xem lỗi có hiển thị không
        Espresso.onView(withId(R.id.loginEmail)).check(matches(hasErrorText("Error")));
    }


    @Test
    public void testEmptyFieldValidation() {
        // Nhấp nút đăng nhập mà không nhập dữ liệu
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        // Kiểm tra xem lỗi có hiển thị không
        Espresso.onView(withId(R.id.loginEmail)).check(matches(hasErrorText("Error")));
        Espresso.onView(withId(R.id.loginPassword)).check(matches(hasErrorText("Error")));
    }
}
