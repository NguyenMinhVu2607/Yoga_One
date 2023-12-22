package com.at17.kma.yogaone.Fragment_Coach;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.at17.kma.yogaone.Login_Res.LoginActivity;
import com.at17.kma.yogaone.R;
import com.at17.kma.yogaone.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileCoachFragment extends Fragment {
    private Button btnLogout;
    private ImageView profileImage;
    private TextView usernameTextView, emailTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_coach, container, false);
        btnLogout = view.findViewById(R.id.btnLogout);
        profileImage = view.findViewById(R.id.profileImage);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SplashActivity.class));
            }
        });
        loadUserInfo();
        return view;
    }
    private void loadUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Load username and email
            usernameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
            Log.d("name",""+user.getDisplayName());

            // Load profile image
            // You may need to implement your own logic to load the image based on the user's ID
            // For example, you can use Firebase Storage to store user profile images.
            // Here, we just set a default image for illustration purposes.
            profileImage.setImageResource(R.drawable.icon_profile);
        }
    }
}