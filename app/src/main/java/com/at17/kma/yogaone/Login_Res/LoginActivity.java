package com.at17.kma.yogaone.Login_Res;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1101;
    EditText email,password;
    Button loginBtn , gotoRes;
    boolean valid = true;
    FirebaseAuth fAuthLogin;
    FirebaseFirestore fFirestore;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    SharedPreferences sharedPreferences;
    ImageButton biometricLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         biometricLoginButton = findViewById(R.id.biometric_login);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginbtn);
        gotoRes = findViewById(R.id.gotoRegister);
        //FireBase
        fAuthLogin = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();
//        AnhXaView();
        BiometricManager biometricManager = BiometricManager.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    Toast.makeText(this, "App có thể dùng vân tay", Toast.LENGTH_SHORT).show();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Toast.makeText(this, "App không thể dùng vân tay", Toast.LENGTH_SHORT).show();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    // Prompts the user to create credentials that your app accepts.
                    final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                    startActivityForResult(enrollIntent, REQUEST_CODE);
                    break;
            }
        }
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                String email = sharedPreferences.getString("email","");
                String password = sharedPreferences.getString("password","");
                LoginFuntion(email,password);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for Yoga Studio")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });



        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin",false);
        if (isLogin){
            String email = sharedPreferences.getString("email","");
            String password = sharedPreferences.getString("password","");
            biometricLoginButton.setVisibility(View.VISIBLE);
        }

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
                String emails = email.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                LoginFuntion(emails,passwords);
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
//    public void AnhXaView(){
//        email = findViewById(R.id.loginEmail);
//        password = findViewById(R.id.loginPassword);
//        loginBtn = findViewById(R.id.loginbtn);
//        gotoRes = findViewById(R.id.gotoRegister);
//
//    }
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
    public void LoginFuntion (String email1, String password1){
        // Call Fun Check Edittext
        CheckField(email);
        CheckField(password);
        if (valid){
            fAuthLogin.signInWithEmailAndPassword(email1,password1)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences("dataLogin",MODE_PRIVATE).edit();
                            editor.putString("email", String.valueOf(email));
                            editor.putString("password", String.valueOf(password));
                            editor.putBoolean("isLogin",true);
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
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            DocumentReference documentReference = fFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid());
//            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    Log.d("tag",""+documentSnapshot.getData());
//                    if(documentSnapshot.getString("isUser") != null ){
//                        // If User is HV
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        finish();
//                    } else if (documentSnapshot.getString("isCoach") != null){
//                        // If User is Coach
//                        startActivity(new Intent(getApplicationContext(), CoachActivity.class));
//                        finish();
//                    }
//                }
//            });
//        }
//    }
