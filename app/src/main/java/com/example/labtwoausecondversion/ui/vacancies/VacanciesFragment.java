package com.example.labtwoausecondversion.ui.vacancies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.IRecyclerViewClickCallback;
import com.example.labtwoausecondversion.ui.adapter.VacanciesAdapter;
import com.example.labtwoausecondversion.ui.browse.BrowseActivity;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.victor.loading.rotate.RotateLoading;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VacanciesFragment extends Fragment implements VacanciesContract.View, IRecyclerViewClickCallback {
    @BindView(R.id.swipy_refresh_layout)
    SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rotate_loading)
    RotateLoading rotateLoading;

    private Unbinder mUnbinder;
    private VacanciesPresenter mPresenter;
    private VacanciesAdapter mAdapter;
    private int mPage;

    @Override
    public void showIndicator() {
        rotateLoading.start();
    }

    @Override
    public void hideIndicator() {
        rotateLoading.stop();
    }

    @Override
    public boolean isIndicatorShown() {
        return rotateLoading.isShown();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancies, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        swipyRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipyRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        swipyRefreshLayout.setRefreshing(false);
        mPresenter = new VacanciesPresenter(new ResourceHelper(getContext()));
        mPresenter.bind(this);
        mPage = 1;
        mPresenter.getVacancies(mPage);
        return view;
    }

    @Override
    public void onGetVacanciesSuccess(List<VacancyModel> vacancyModels) {
        if (vacancyModels != null) {
            if (mPage == 1) {
                if (recyclerView.getAdapter() != null) {
                    recyclerView.setAdapter(null);
                }
                mAdapter = new VacanciesAdapter(new ResourceHelper(getContext()), vacancyModels, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(mAdapter);
            } else if (mPage > 1) {
                mAdapter.addVacancyModels(vacancyModels);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGetVacanciesError(String msg) {
        Snackbar.make(recyclerView, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCheckBoxClick(boolean isChecked, VacancyModel vacancyModel) {
        if (isChecked) {
            Snackbar.make(recyclerView, R.string.add_to_fav, Snackbar.LENGTH_LONG).show();
            mPresenter.saveFavoriteVacancy(vacancyModel);
        } else if (!isChecked){
            Snackbar.make(recyclerView, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
            mPresenter.deleteFavoriteVacancy(vacancyModel.getPid());
        }
    }

    @Override
    public void onItemViewClick(int position) {
        Intent browseIntent = new Intent(getContext(), BrowseActivity.class);
        browseIntent.putParcelableArrayListExtra(getString(R.string.vacancy_list), mAdapter.getVacancyModels());
        browseIntent.putExtra(getString(R.string.position), position);
        startActivityForResult(browseIntent, AppConstants.REQUEST_CODE);
    }

    /* receive data from BrowseActivity */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE && data != null) {
            if (mAdapter != null) {
                mAdapter.changeVacancyModelsState(data.<VacancyModel>getParcelableArrayListExtra(getString(R.string.vacancy_list)));
            }
        }
    }

    private SwipyRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipyRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(SwipyRefreshLayoutDirection direction) {
            swipyRefreshLayout.setRefreshing(true);

            swipyRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mPage++;
                    mPresenter.getVacancies(mPage);
                    swipyRefreshLayout.setRefreshing(false);
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unbind();
    }
}
