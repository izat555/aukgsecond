package com.example.labtwoausecondversion.ui.search_result;

import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.ILifeCycle;
import com.example.labtwoausecondversion.ui.IProgressBar;

import java.util.List;

public interface SearchResultContract {
    interface View extends IProgressBar {
        void onGetSearchVacanciesSuccess(List<VacancyModel> vacancyModels);
        void onGetSearchVacanciesError(String msg);
    }

    interface Presenter extends ILifeCycle<View> {
        void getSearchVacancies(int page, String rubric, String salary, String term, String prof);
        void saveFavoriteVacancy(VacancyModel vacancyModel);
        void deleteFavoriteVacancy(String pid);
    }
}
