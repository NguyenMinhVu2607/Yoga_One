package com.at17.kma.yogaone.Login_Res;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResActivity extends AppCompatActivity {
    EditText fullName,email,password,phone;
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
        // Go to login Code
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(ResActivity.this,LoginActivity.class));
//                finish();
            }
        });


        // Create code
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call Fun Check Edittext
                CheckField(fullName);
                CheckField(email);
                CheckField(password);
                CheckField(phone);

                // Create Account FireBase
                if(valid){
                    fAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            Toast.makeText(ResActivity.this, "Account Create", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = fFirestore.collection("Users").document(firebaseUser.getUid());
                            Map<String,Object> userInfo =new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("UserMail",email.getText().toString());
                            userInfo.put("PhoneNumber",phone.getText().toString());

                            //Specify if the user is coach
                            userInfo.put("isUser",1);
                            //
                            documentReference.set(userInfo);
                            startActivity(new Intent(ResActivity.this, LoginActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ResActivity.this, "Error Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
    public void AnhXaView(){
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerbtn);
        gotoLogin = findViewById(R.id.gotoLogin);

    }
    public Boolean CheckField(EditText textField){
        //Check Edittext
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

}