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

public class CoachActivity  extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);
        bottomNavigationView
                = findViewById(R.id.bottomNavigationViewCoach);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeCoach);
     }
    int selectedFragmentIdCoach;

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.manageCourse) {
            selectedFragmentIdCoach = R.id.manageCourse;
        } else if (item.getItemId() == R.id.homeCoach) {
            selectedFragmentIdCoach = R.id.homeCoach;
        } else if (item.getItemId() == R.id.profileCoach) {
            selectedFragmentIdCoach = R.id.profileCoach;
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

        return true;
    }
}
