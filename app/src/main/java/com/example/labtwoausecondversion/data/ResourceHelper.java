package com.example.labtwoausecondversion.data;

import android.content.Context;

public class ResourceHelper {
    private Context mContext;

    public ResourceHelper(Context context) {
        mContext = context;
    }

    public Context getContext() {
        if (mContext != null) {
            return mContext;
        }
        return null;
    }
}
