package com.ragshion.sakpore.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ragshion.sakpore.R;


public class SplashscreenActivity extends AppCompatActivity {

    MaterialDialog materialDialog;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
            SplashscreenActivity.this.startActivity(intent);
            SplashscreenActivity.this.finish();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashscreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);


        setContentView(R.layout.activity_splashscreen);

    }



}

