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
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    // Khai báo biến lưu trạng thái Fragment hiện tại
    int currentFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome to Yoga Studio", Toast.LENGTH_SHORT).show();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.homeStudent);

        // Khởi tạo trạng thái Fragment ban đầu
        currentFragmentId = R.id.homeStudent;
    }

    // Cài đặt sự kiện khi chọn mục trên thanh bottomNavigationView
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int selectedFragmentId = item.getItemId();

        // Kiểm tra nếu mục chọn trùng với trạng thái hiện tại của Fragment
        if (selectedFragmentId == currentFragmentId) {
            return false; // Không thực hiện thay đổi Fragment
        }

        Fragment fragment;

        // Tạo Fragment tương ứng với mục đã chọn
        if (selectedFragmentId == R.id.listAllClass) {
            fragment = new CourseFragment();
        } else if (selectedFragmentId == R.id.homeStudent) {
            fragment = new HomeFragment();
        } else if (selectedFragmentId == R.id.listClassConfirm) {
            fragment = new ListClassConfirmFragment();
        } else {
            fragment = new ProfileFragment();
        }

        // Thay đổi Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();

        // Cập nhật trạng thái Fragment hiện tại
        currentFragmentId = selectedFragmentId;

        return true;
    }
}
