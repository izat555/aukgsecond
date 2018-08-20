package com.example.labtwoausecondversion.ui.search_result;

import com.example.labtwoausecondversion.AuApp;
import com.example.labtwoausecondversion.BuildConfig;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.SQLiteHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultPresenter implements SearchResultContract.Presenter {
    private ResourceHelper mResourceHelper;
    private SearchResultContract.View mView;
    private SQLiteHelper mSQLiteHelper;

    public SearchResultPresenter(ResourceHelper resourceHelper) {
        mResourceHelper = resourceHelper;
        mSQLiteHelper = new SQLiteHelper(mResourceHelper.getContext());
    }

    @Override
    public void bind(SearchResultContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    @Override
    public void getSearchVacancies(int page, String rubric, String salary, String term, String prof) {
        if (mView != null && page == 1) {
            mView.showIndicator();
        }

        if (mResourceHelper != null) {
            Call<List<VacancyModel>> call = AuApp
                    .get(mResourceHelper.getContext()).getRetrofitService()
                    .searchVacancies(BuildConfig.LOGIN, AppConstants.F_SEARCH, AppConstants.COUNT,
                            page, rubric, salary, term, prof);

            call.enqueue(new Callback<List<VacancyModel>>() {
                @Override
                public void onResponse(Call<List<VacancyModel>> call, Response<List<VacancyModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<VacancyModel> favoriteVacancies = mSQLiteHelper.getAllVacancies();
                        List<VacancyModel> fetchedVacancies = response.body();
                        for (int i = 0; i < fetchedVacancies.size(); i++) {
                            for (VacancyModel model : favoriteVacancies) {
                                if (model.getPid().equals(fetchedVacancies.get(i).getPid())) {
                                    fetchedVacancies.get(i).setChecked(true);
                                }
                            }
                        }
                        if (mView != null) {
                            mView.onGetSearchVacanciesSuccess(fetchedVacancies);
                            if (mView.isIndicatorShown()) {
                                mView.hideIndicator();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<VacancyModel>> call, Throwable t) {
                    if (mView != null) {
                        mView.onGetSearchVacanciesError(t.getMessage());
                        if (mView.isIndicatorShown()) {
                            mView.hideIndicator();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void saveFavoriteVacancy(VacancyModel vacancyModel) {
        mSQLiteHelper.saveSingleVacancy(vacancyModel);
    }

    @Override
    public void deleteFavoriteVacancy(String pid) {
        mSQLiteHelper.deleteSingleVacancy(pid);
    }
}
