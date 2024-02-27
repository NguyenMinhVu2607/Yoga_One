package com.hunre.dh10c6.yogaone;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.hunre.dh10c6.yogaone.Fragment_Coach.HomeCoachFragment;
import com.hunre.dh10c6.yogaone.Fragment_Coach.ManageCourseFragment;
import com.hunre.dh10c6.yogaone.Fragment_Coach.ProfileCoachFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoachActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    int currentFragmentIdCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewCoach);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeCoach);
        currentFragmentIdCoach = R.id.homeCoach;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int selectedFragmentIdCoach = item.getItemId();

        if (selectedFragmentIdCoach == currentFragmentIdCoach) {
            return false;
        }

        Fragment fragment;

        if (selectedFragmentIdCoach == R.id.manageCourse) {
            fragment = new ManageCourseFragment();
        } else if (selectedFragmentIdCoach == R.id.homeCoach) {
            fragment = new HomeCoachFragment();
        } else {
            fragment = new ProfileCoachFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();

        currentFragmentIdCoach = selectedFragmentIdCoach;

        return true;
    }
}
