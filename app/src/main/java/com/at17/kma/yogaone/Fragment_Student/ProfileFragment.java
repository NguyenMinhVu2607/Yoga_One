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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
        listenForEmailVerification();
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

        checkEmailVerificationStatus();
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
    // Trong hàm checkEmailVerificationStatus
    private void checkEmailVerificationStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Kiểm tra xem email đã được xác minh chưa
            if (!user.isEmailVerified()) {
                Log.d("Ver", "" + user.isEmailVerified());
                Toast.makeText(getContext(), "Email is Not Verified", Toast.LENGTH_SHORT).show();

                sendCode.setVisibility(View.VISIBLE);
                sendCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Gửi email xác minh
                        user.sendEmailVerification().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Gửi email xác minh thành công
                                    Toast.makeText(getContext(), "Thông tin xác thực đã được gửi thành công đến Email của bạn !", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Gửi email xác minh thất bại
                                    Toast.makeText(getContext(), "Thông tin xác thực chưa được gửi :)", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            } else {
                // Email đã được xác minh
                sendCode.setVisibility(View.GONE);
                infoVerify.setText("Email is Verified");
                infoVerify.setTextColor(getResources().getColor(R.color.cpp_text));
                Toast.makeText(getContext(), "Email is Verified", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Thêm hàm sau để cập nhật ngay lập tức khi xác minh email thành công
    private void listenForEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Sau khi reload, kiểm tra trạng thái xác minh mới
                    checkEmailVerificationStatus();
                }
            });
        }
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