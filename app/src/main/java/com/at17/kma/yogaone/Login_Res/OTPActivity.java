package com.at17.kma.yogaone.Login_Res;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.at17.kma.yogaone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {
    EditText otpEditText;
    Button verifyOTPBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;

    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        fAuth = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();

        otpEditText = findViewById(R.id.otpEditText);
        verifyOTPBtn = findViewById(R.id.verifyOTPBtn);

        verificationId = getIntent().getStringExtra("verificationId");

        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otpEditText.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otpEditText.setError("Enter valid code");
                    otpEditText.requestFocus();
                    return;
                }
                verifyOTP(code);
            }
        });
    }

    private void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = fAuth.getCurrentUser();
                    Toast.makeText(OTPActivity.this, "Phone number verified", Toast.LENGTH_SHORT).show();

                    DocumentReference documentReference = fFirestore.collection("Users").document(firebaseUser.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("FullName", "Set your full name"); // Set full name as per your requirement
                    userInfo.put("UserMail", "Set your email"); // Set email as per your requirement
                    userInfo.put("PhoneNumber", firebaseUser.getPhoneNumber());

                    // Additional user information or logic can be added here

                    documentReference.set(userInfo)
                            .addOnSuccessListener(aVoid -> {
                                startActivity(new Intent(OTPActivity.this, LoginActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("TAG", "Error adding user info to Firestore.", e);
                            });
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(OTPActivity.this, "Invalid code entered", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("TAG", "Error signing in with phone auth credential.", e);
                    }
                });
    }
}
