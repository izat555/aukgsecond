package com.example.labtwoausecondversion.ui.search_result;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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

public class SearchResultActivity extends AppCompatActivity implements SearchResultContract.View, IRecyclerViewClickCallback {
    @BindView(R.id.custom_toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipy_refresh_layout)
    SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rotate_loading)
    RotateLoading rotateLoading;

    private Unbinder mUnbinder;
    private SearchResultPresenter mPresenter;
    private VacanciesAdapter mAdapter;
    private int mPage;

    private String mRubric;
    private String mSalary;
    private String mTerm;
    private String mProf;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mUnbinder = ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.search));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipyRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipyRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        swipyRefreshLayout.setRefreshing(false);

        mPresenter = new SearchResultPresenter(new ResourceHelper(this));
        mPresenter.bind(this);
        mPage = 1;

        if (getIntent() != null) {
            mRubric = getIntent().getStringExtra(getString(R.string.rubric_tag));
            mSalary = getIntent().getStringExtra(getString(R.string.salary_tag));
            mTerm = getIntent().getStringExtra(getString(R.string.term_tag));
            mProf = getIntent().getStringExtra(getString(R.string.prof_tag));
            mPresenter.getSearchVacancies(mPage, mRubric, mSalary, mTerm, mProf);
        }

    }

    @Override
    public void onGetSearchVacanciesSuccess(List<VacancyModel> vacancyModels) {
        if (vacancyModels != null && mPage == 1) {
            mAdapter = new VacanciesAdapter(new ResourceHelper(this), vacancyModels, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
        } else if (vacancyModels != null && mPage > 1) {
            mAdapter.addVacancyModels(vacancyModels);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetSearchVacanciesError(String msg) {
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
        Intent browseIntent = new Intent(this, BrowseActivity.class);
        browseIntent.putParcelableArrayListExtra(getString(R.string.vacancy_list), mAdapter.getVacancyModels());
        browseIntent.putExtra(getString(R.string.position), position);
        startActivityForResult(browseIntent, AppConstants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE && data != null) {
            mAdapter.changeVacancyModelsState(data.<VacancyModel>getParcelableArrayListExtra(getString(R.string.vacancy_list)));
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
                    mPresenter.getSearchVacancies(mPage, mRubric, mSalary, mTerm, mProf);
                    swipyRefreshLayout.setRefreshing(false);
                }
            });
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unbind();
    }
}
