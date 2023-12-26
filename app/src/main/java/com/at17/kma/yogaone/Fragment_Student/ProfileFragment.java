package com.at17.kma.yogaone.Fragment_Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.at17.kma.yogaone.AddressListActivity;
import com.at17.kma.yogaone.Login_Res.LoginActivity;
import com.at17.kma.yogaone.Login_Res.ResActivity;
import com.at17.kma.yogaone.R;
import com.at17.kma.yogaone.SplashActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    CircleImageView logout;
    Button sendCode;
    TextView infoVerify;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ConstraintLayout showAddressesButton = view.findViewById(R.id.showAddressesButton);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser() ;
        String userID = firebaseAuth.getUid();
        showAddressesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddresses();
            }
        });
        sendCode=view.findViewById(R.id.sendCode);
        infoVerify=view.findViewById(R.id.infoVerify);

        if(!user.isEmailVerified()){
            Log.d("Ver",""+user.isEmailVerified());
            Toast.makeText(getContext(), "Email is Not Verify", Toast.LENGTH_SHORT).show();

            sendCode.setVisibility(View.VISIBLE);
            sendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Verification Email not Sent", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });

        } else {
            infoVerify.setText("Email is Verify");
            infoVerify.setTextColor(getResources().getColor(R.color.black));
            Toast.makeText(getContext(), "Email is Verify", Toast.LENGTH_SHORT).show();
        }

        // Đăng xuất tài khoản
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