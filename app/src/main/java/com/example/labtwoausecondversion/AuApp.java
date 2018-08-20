package com.example.labtwoausecondversion;

import android.app.Application;
import android.content.Context;

import com.example.labtwoausecondversion.data.network.NetworkBuilder;
import com.example.labtwoausecondversion.data.network.RetrofitService;

public class AuApp extends Application {
    private RetrofitService mRetrofitService;

    @Override
    public void onCreate() {
        super.onCreate();
        mRetrofitService = NetworkBuilder.initRetrofitService();
    }

    public static AuApp get(Context context) {
        return (AuApp) context.getApplicationContext();
    }

    public RetrofitService getRetrofitService() {
        return mRetrofitService;
    }
}
