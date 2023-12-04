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

import com.at17.kma.yogaone.CoachActivity;
import com.at17.kma.yogaone.MainActivity;
import com.at17.kma.yogaone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button loginBtn , gotoRes;
    boolean valid = true;
    FirebaseAuth fAuthLogin;
    FirebaseFirestore fFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FireBase
        fAuthLogin = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();
        AnhXaView();
        //Go to Res
        gotoRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResActivity.class));
                finish();
            }
        });

        // Login Code

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call Fun Check Edittext
                CheckField(email);
                CheckField(password);
                Log.d("TAG",""+email.getText().toString());
                if (valid){
                    fAuthLogin.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    checkUserLevelAccess(authResult.getUser().getUid());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
    private void checkUserLevelAccess(String uid) {
        DocumentReference documentReference = fFirestore.collection("Users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("tag",""+documentSnapshot.getData());
                if(documentSnapshot.getString("isUser") != null ){
                    // If User is HV
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else if (documentSnapshot.getString("isCoach") != null){
                    // If User is Coach
                    startActivity(new Intent(getApplicationContext(), CoachActivity.class));
                    finish();
                }
            }
        });
    }
    public void AnhXaView(){
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginbtn);
        gotoRes = findViewById(R.id.gotoRegister);

    }
    public boolean CheckField(EditText textField){
        //Check Edittext
        if (textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference documentReference = fFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("tag",""+documentSnapshot.getData());
                    if(documentSnapshot.getString("isUser") != null ){
                        // If User is HV
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    } else if (documentSnapshot.getString("isCoach") != null){
                        // If User is Coach
                        startActivity(new Intent(getApplicationContext(), CoachActivity.class));
                        finish();
                    }
                }
            });
        }
    }
}