package com.ragshion.sakpore.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ragshion.sakpore.R;

import spencerstudios.com.bungeelib.Bungee;

public class TentangActivity extends AppCompatActivity {

    int RESULT_CODE = 1;

    MaterialDialog materialDialog;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Bungee.swipeRight(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Bungee.swipeRight(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tentang Aplikasi");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_CODE){
            finish();
            Bungee.fade(this);
        }
    }
}
