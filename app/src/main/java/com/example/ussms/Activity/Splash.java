package com.example.ussms.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ussms.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_splash);
        mAuth = FirebaseAuth.getInstance();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() !=null){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(Splash.this, Main2Activity.class));
                    finish();
                }

            }
        },800);
        //startActivity(new Intent(Splash.this, Main2Activity.class));
    }



}