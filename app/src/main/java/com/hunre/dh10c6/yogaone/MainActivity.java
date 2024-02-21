package com.hunre.dh10c6.yogaone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.hunre.dh10c6.yogaone.Fragment_Student.CourseFragment;
import com.hunre.dh10c6.yogaone.Fragment_Student.HomeFragment;
import com.hunre.dh10c6.yogaone.Fragment_Student.ListClassConfirmFragment;
import com.hunre.dh10c6.yogaone.Fragment_Student.ProfileFragment;
//import com.github.kwasow.bottomnavigationcircles.BottomNavigationCircles;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome to Yoga Studio", Toast.LENGTH_SHORT).show();
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
        if (item.getItemId() == R.id.listClassConfirm) {
            selectedFragmentId = R.id.listClassConfirm;
        } else if (item.getItemId() == R.id.homeStudent) {
            selectedFragmentId = R.id.homeStudent;
        } else if (item.getItemId() == R.id.profileStudent) {
            selectedFragmentId = R.id.profileStudent;
        } else if (item.getItemId() == R.id.listAllClass) {
            selectedFragmentId = R.id.listAllClass;
        }

        Fragment fragment;

        if (selectedFragmentId == R.id.listAllClass) {
            fragment = new CourseFragment();
        } else if (selectedFragmentId == R.id.homeStudent) {
            fragment = new HomeFragment();
        } else if (selectedFragmentId == R.id.listClassConfirm) {
            fragment = new ListClassConfirmFragment();
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