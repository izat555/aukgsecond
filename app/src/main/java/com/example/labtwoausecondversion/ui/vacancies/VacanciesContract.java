package com.example.labtwoausecondversion.ui.vacancies;

import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.ILifeCycle;
import com.example.labtwoausecondversion.ui.IProgressBar;

import java.util.List;

public interface VacanciesContract {

    interface View extends IProgressBar {
        void onGetVacanciesSuccess(List<VacancyModel> vacancyModels);
        void onGetVacanciesError(String msg);
    }

    interface Presenter extends ILifeCycle<View> {
        void getVacancies(int page);
        void saveFavoriteVacancy(VacancyModel vacancyModel);
        void deleteFavoriteVacancy(String pid);
        void saveLastVacancies(List<VacancyModel> vacancyModels);
    }
}
