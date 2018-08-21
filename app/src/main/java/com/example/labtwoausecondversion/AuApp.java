package com.example.labtwoausecondversion;

import android.app.Application;
import android.content.Context;

import com.example.labtwoausecondversion.data.SQLiteHelper;
import com.example.labtwoausecondversion.data.network.NetworkBuilder;
import com.example.labtwoausecondversion.data.network.RetrofitService;

public class AuApp extends Application {
    private RetrofitService mRetrofitService;
    private SQLiteHelper mSQLiteHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mRetrofitService = NetworkBuilder.initRetrofitService();
        mSQLiteHelper = new SQLiteHelper(getApplicationContext());
    }

    public static AuApp get(Context context) {
        return (AuApp) context.getApplicationContext();
    }

    public RetrofitService getRetrofitService() {
        return mRetrofitService;
    }

    public SQLiteHelper getSQLiteHelper() {
        return mSQLiteHelper;
    }
}
