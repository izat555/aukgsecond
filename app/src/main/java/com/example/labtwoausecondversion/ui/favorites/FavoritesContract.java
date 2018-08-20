package com.example.labtwoausecondversion.ui.favorites;

import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.ILifeCycle;
import com.example.labtwoausecondversion.ui.IProgressBar;

import java.util.ArrayList;

public interface FavoritesContract {
    interface View extends IProgressBar {
        void onGetVacancies(ArrayList<VacancyModel> vacancyModels);
    }

    interface Presenter extends ILifeCycle<View> {
        void getVacancies();
        void deleteFavoriteVacancy(String pid);
    }
}
