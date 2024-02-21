package com.hunre.dh10c6.yogaone;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> activityRule = new ActivityTestRule<>(SplashActivity.class, true, false);

    @Test
    public void testSplashScreen() {
        // Tạo một Intent để mô phỏng việc chuyển từ SplashActivity sang LoginActivity
        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        // Kiểm tra xem SplashActivity có được hiển thị không
        onView(withId(R.id.splashscreen)).check(matches(isDisplayed()));

        // Chờ một thời gian ngắn (tùy thuộc vào thời gian hiển thị Splash Screen)
        try {
            Thread.sleep(2000); // Đợi 2 giây
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Kiểm tra xem LoginActivity có được mở sau khi SplashActivity
        // (Bạn có thể thay đổi thành MainActivity hoặc màn hình mong muốn)
        onView(withId(R.id.login_layout)).check(matches(isDisplayed()));
    }
}
