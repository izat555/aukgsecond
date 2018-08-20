package com.example.labtwoausecondversion.ui.favorites;

import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.SQLiteHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;

import java.util.ArrayList;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private ResourceHelper mResourceHelper;
    private FavoritesContract.View mView;
    private SQLiteHelper mSQLiteHelper;

    @Override
    public void bind(FavoritesContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    public FavoritesPresenter(ResourceHelper resourceHelper) {
        mResourceHelper = resourceHelper;
    }

    @Override
    public void getVacancies() {
        if (mView != null) {
            mView.showIndicator();
        }

        if (mResourceHelper != null) {
            mSQLiteHelper = new SQLiteHelper(mResourceHelper.getContext());
            ArrayList<VacancyModel> vacancyModels = mSQLiteHelper.getAllVacancies();
            for (VacancyModel model : vacancyModels) {
                model.setChecked(true);
            }
            if (mView != null) {
                mView.onGetVacancies(vacancyModels);
                if (mView.isIndicatorShown()) {
                    mView.hideIndicator();
                }
            }
        }
    }

    @Override
    public void deleteFavoriteVacancy(String pid) {
        mSQLiteHelper.deleteSingleVacancy(pid);
    }
}
