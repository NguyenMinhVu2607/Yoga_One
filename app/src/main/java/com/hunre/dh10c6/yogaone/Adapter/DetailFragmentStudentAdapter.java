package com.hunre.dh10c6.yogaone.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hunre.dh10c6.yogaone.Fragment_Coach.ListStudentClassFragment;
import com.hunre.dh10c6.yogaone.Fragment_Coach.RequestStudentFragment;

public class DetailFragmentStudentAdapter extends FragmentStatePagerAdapter {


    private Bundle bundle;

    public DetailFragmentStudentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListStudentClassFragment listStudentClassFragment = new ListStudentClassFragment();
                listStudentClassFragment.setArguments(bundle); // Truyền dữ liệu từ Bundle vào Fragment
                return listStudentClassFragment;
            case 1:
                RequestStudentFragment fragment = new RequestStudentFragment();
                fragment.setArguments(bundle); // Truyền dữ liệu từ Bundle vào Fragment
                return fragment;
            default: return new ListStudentClassFragment();
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "List Student";
                break;
            case 1:
                title = "List Request";
                break;
        }
        return title;
    }
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
        notifyDataSetChanged(); // Cập nhật ViewPager khi có dữ liệu mới
    }

}








