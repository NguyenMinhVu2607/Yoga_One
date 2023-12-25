package com.at17.kma.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.at17.kma.yogaone.AddressListActivity;
import com.at17.kma.yogaone.Login_Res.LoginActivity;
import com.at17.kma.yogaone.R;
import com.at17.kma.yogaone.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    CircleImageView logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ConstraintLayout showAddressesButton = view.findViewById(R.id.showAddressesButton);
        showAddressesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddresses();
            }
        });
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SplashActivity.class));
            }
        });
        return  view;
    }
    public void showAddresses() {
        // Lấy danh sách địa chỉ từ resources
//        String[] addresses = getResources().getStringArray(R.array.location_array);

        // Truyền danh sách địa chỉ sang màn hình mới
        Intent intent = new Intent(getContext(), AddressListActivity.class);
//        intent.putExtra("addresses", addresses);
        startActivity(intent);
    }
}