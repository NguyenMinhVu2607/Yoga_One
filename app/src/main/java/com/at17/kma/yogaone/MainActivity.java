package com.at17.kma.yogaone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.at17.kma.yogaone.Fragment_Student.CourseFragment;
import com.at17.kma.yogaone.Fragment_Student.HomeFragment;
import com.at17.kma.yogaone.Fragment_Student.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeStudent);
    }
    CourseFragment firstFragment = new CourseFragment();
    HomeFragment secondFragment = new HomeFragment();
    ProfileFragment thirdFragment = new ProfileFragment();

    int selectedFragmentId;

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.person) {
            selectedFragmentId = R.id.person;
        } else if (item.getItemId() == R.id.homeStudent) {
            selectedFragmentId = R.id.homeStudent;
        } else if (item.getItemId() == R.id.profileStudent) {
            selectedFragmentId = R.id.profileStudent;
        }

        Fragment fragment;

        if (selectedFragmentId == R.id.person) {
            fragment = new CourseFragment();
        } else if (selectedFragmentId == R.id.homeStudent) {
            fragment = new HomeFragment();
        } else {
            fragment = new ProfileFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();

        return true;
    }

}