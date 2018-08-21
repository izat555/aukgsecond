package com.example.labtwoausecondversion.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.example.labtwoausecondversion.AuApp;
import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.InternetUtils;
import com.example.labtwoausecondversion.data.SQLiteHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.main.MainActivity;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private RotateLoading mRotateLoading;
    private SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRotateLoading = findViewById(R.id.rotate_loading);
        mRotateLoading.start();

        mSQLiteHelper = AuApp.get(this).getSQLiteHelper();

        if (InternetUtils.isInternetConnected(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    mRotateLoading.stop();
                    finish();
                }
            }, 1000*3);
        } else {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<VacancyModel> vacancyModels = mSQLiteHelper.getLastSavedVacancies();
                    ArrayList<VacancyModel> suitableModels = mSQLiteHelper.getLastSavedSuitable();
                    if (!vacancyModels.isEmpty()) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putParcelableArrayListExtra(getString(R.string.vacancy_list), vacancyModels);
                        intent.putParcelableArrayListExtra(getString(R.string.suitable_list), suitableModels);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 1000*1);
        }
    }
}
