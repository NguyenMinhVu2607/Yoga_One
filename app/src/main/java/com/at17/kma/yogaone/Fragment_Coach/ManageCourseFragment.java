package com.at17.kma.yogaone.Fragment_Coach;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.at17.kma.yogaone.CoachFuntion.AddClassActivity;
import com.at17.kma.yogaone.R;
public class ManageCourseFragment extends Fragment {

    private Button btnAddClass;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_manage_course, container, false);
        btnAddClass = view.findViewById(R.id.btnAddClass);
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddClassActivity.class));
            }
        });

        return view;
    }
}