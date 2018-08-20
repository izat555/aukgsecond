package com.example.labtwoausecondversion.data.entity;


import android.support.v4.app.Fragment;

public class TabItemModel {
    private final Fragment mFragment;
    private final String mTitle;

    public TabItemModel(Fragment fragment, String title) {
        mFragment = fragment;
        mTitle = title;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public String getTitle() {
        return mTitle;
    }
}
