package com.at17.kma.yogaone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.at17.kma.yogaone.Login_Res.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
   FirebaseFirestore fFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fFirestore = FirebaseFirestore.getInstance();

//        LoadScreen();
    }
    public void LoadScreen() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }, 1500); // you can increase or decrease the timelimit of your screen
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
        } else {
            LoadScreen();
        }
    }
}