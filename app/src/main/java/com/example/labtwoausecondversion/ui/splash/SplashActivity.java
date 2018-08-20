package com.example.labtwoausecondversion.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.data.SQLiteHelper;
import com.example.labtwoausecondversion.ui.main.MainActivity;
import com.victor.loading.rotate.RotateLoading;

public class SplashActivity extends AppCompatActivity {
    private RotateLoading mRotateLoading;
    private SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSQLiteHelper = new SQLiteHelper(this);

        mRotateLoading = findViewById(R.id.rotate_loading);
        mRotateLoading.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                mRotateLoading.stop();
                finish();
            }
        }, 1000*3);
    }
}
