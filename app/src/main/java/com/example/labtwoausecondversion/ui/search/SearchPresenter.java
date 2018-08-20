package com.example.labtwoausecondversion.ui.search;

import com.example.labtwoausecondversion.AuApp;
import com.example.labtwoausecondversion.BuildConfig;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.entity.RubricModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter implements SearchContract.Presenter {
    private ResourceHelper mResourceHelper;
    private SearchContract.View  mView;

    public SearchPresenter(ResourceHelper resourceHelper) {
        mResourceHelper = resourceHelper;
    }

    @Override
    public void bind(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    @Override
    public void getCategories() {
        if (mView != null) {
            mView.showIndicator();
        }

        if (mResourceHelper != null) {
            Call<List<RubricModel>> call = AuApp.get(mResourceHelper.getContext())
                    .getRetrofitService().getRubrics(BuildConfig.LOGIN, AppConstants.F_GET_RUBRICS);

            call.enqueue(new Callback<List<RubricModel>>() {
                @Override
                public void onResponse(Call<List<RubricModel>> call, Response<List<RubricModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<RubricModel> rubricModels = response.body();
                        ArrayList<String> rubrics = new ArrayList<>();
                        for (RubricModel model : rubricModels) {
                            rubrics.add(model.getRubric());
                        }
                        if (mView != null) {
                            mView.onGetCategoriesCallback(rubrics);
                            if (mView.isIndicatorShown()) {
                                mView.hideIndicator();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<RubricModel>> call, Throwable t) {
                    if (mView != null) {
                        mView.onGetCategoriesError(t.getMessage());
                        if (mView.isIndicatorShown()) {
                            mView.hideIndicator();
                        }
                    }
                }
            });
        }
    }
}
