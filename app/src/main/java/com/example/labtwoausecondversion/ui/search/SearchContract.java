package com.example.labtwoausecondversion.ui.search;

import com.example.labtwoausecondversion.ui.ILifeCycle;
import com.example.labtwoausecondversion.ui.IProgressBar;

import java.util.ArrayList;

public interface SearchContract {
    interface View extends IProgressBar {
        void onGetCategoriesCallback(ArrayList<String> rubrics);
        void onGetCategoriesError(String msg);
    }

    interface Presenter extends ILifeCycle<View> {
        void getCategories();
    }
}
