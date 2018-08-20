package com.example.labtwoausecondversion.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.labtwoausecondversion.config.AppConstants;

public class PreferencesHelper {
    private SharedPreferences mPreferences;

    public PreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(AppConstants.STORAGE_TITLE, Context.MODE_PRIVATE);
    }

    public void saveProfessionName(String name) {
        mPreferences.edit().putString(AppConstants.PROF_NAME, name).apply();
    }

    public String getProfessionName() {
        return mPreferences.getString(AppConstants.PROF_NAME, "");
    }
}
