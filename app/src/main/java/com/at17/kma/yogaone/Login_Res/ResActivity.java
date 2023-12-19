package com.at17.kma.yogaone.Login_Res;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.at17.kma.yogaone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResActivity extends AppCompatActivity {
    EditText fullName,email,password,phone;
    EditText confirmPassword;

    Button registerBtn, gotoLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        //FireBase
        fAuth = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();
        AnhXaView();
        addTextWatcher(fullName);
        addTextWatcher(email);
        addTextWatcher(password);
        addTextWatcher(phone);
        // Go to login Code
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResActivity.this,LoginActivity.class));
                finish();
            }
        });


        // Create code
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức kiểm tra EditText
                boolean validFields = checkFields();

                // Tạo tài khoản Firebase
                if (validFields) {
                    fAuth.createUserWithEmailAndPassword(
                                    email.getText().toString().trim(),
                                    password.getText().toString().trim())
                            .addOnSuccessListener(authResult -> {
                                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                                Toast.makeText(ResActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                                // Thêm thông tin người dùng vào Firestore
                                DocumentReference documentReference = fFirestore.collection("Users").document(firebaseUser.getUid());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("FullName", fullName.getText().toString().trim());
                                userInfo.put("UserMail", email.getText().toString().trim());
                                userInfo.put("PhoneNumber", phone.getText().toString().trim());

                                // Xác định xem người dùng có phải là huấn luyện viên không
                                userInfo.put("isUser", "1");
                                //

                                // Thêm tên người dùng vào FirebaseUser
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName.getText().toString().trim())
                                        .build();

                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User profile updated.");
                                            } else {
                                                Log.e("TAG", "Error updating user profile.", task.getException());
                                            }
                                        });

                                documentReference.set(userInfo)
                                        .addOnSuccessListener(aVoid -> {
                                            startActivity(new Intent(ResActivity.this, LoginActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Xử lý lỗi khi thêm thông tin người dùng vào Firestore
                                            Log.e("TAG", "Error adding user info to Firestore.", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Xử lý lỗi tạo tài khoản
                                Log.e("TAG", "Error creating user.", e);
                            });
                }
            }
        });
    }
// Phương thức kiểm tra tất cả các trường
        private boolean checkFields() {
            boolean valid = true;

            valid &= CheckField(fullName);
            valid &= CheckField(email);
            valid &= CheckField(password);
            valid &= CheckField(phone);
            valid &= CheckField(confirmPassword);

            return valid;
        }

        public void AnhXaView(){
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerbtn);
        gotoLogin = findViewById(R.id.gotoLogin);
        confirmPassword = findViewById(R.id.registerConfirmPassword);

    }
    public Boolean CheckField(EditText textField) {
        // Check EditText
        String text = textField.getText().toString().trim();

        if (text.isEmpty()) {
            textField.setError("Field cannot be empty");
            valid = false;
        } else {
            // Additional validation logic based on the ID
            if (textField.getId() == R.id.registerName) {
                if (text.length() > 100) {
                    textField.setError("Full name cannot exceed 100 characters");
                    valid = false;
                } else {
                    valid = true;
                }
            } else if (textField.getId() == R.id.registerEmail) {
                // Use a simple regex pattern to check email format
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!text.matches(emailPattern)) {
                    textField.setError("Invalid email format");
                    valid = false;
                } else {
                    valid = true;
                }
            } else if (textField.getId() == R.id.registerPassword) {
                // Check if the password meets the criteria
                String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
                if (!text.matches(passwordPattern)) {
                    textField.setError("Password must be at least 6 characters with one uppercase letter and one special character");
                    valid = false;
                } else {
                    valid = true;
                }
            } else {
                valid = true;
            }
        }
        if (textField.getId() == R.id.registerConfirmPassword) {
            String enteredPassword = password.getText().toString().trim();
            String confirmPassword = textField.getText().toString().trim();

            if (!confirmPassword.equals(enteredPassword)) {
                textField.setError("Passwords do not match");
                valid = false;
            } else {
                valid = true;
            }
        }
        return valid;
    }

    private void addTextWatcher(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for your case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for your case
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Check the field when text changes
                CheckField(editText);
            }
        });
    }

}