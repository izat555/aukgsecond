package com.example.labtwoausecondversion.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.IRecyclerViewClickCallback;
import com.example.labtwoausecondversion.ui.adapter.VacanciesAdapter;
import com.example.labtwoausecondversion.ui.browse.BrowseActivity;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesActivity extends AppCompatActivity implements FavoritesContract.View, IRecyclerViewClickCallback {
    @BindView(R.id.custom_toolbar)
    Toolbar customToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rotate_loading)
    RotateLoading rotateLoading;

    private Unbinder mUnbinder;
    private FavoritesPresenter mPresenter;
    private VacanciesAdapter mAdapter;

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
        return rotateLoading.isStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mUnbinder = ButterKnife.bind(this);

        customToolbar.setTitle(getString(R.string.favorites));
        customToolbar.findViewById(R.id.ib_toolbar).setVisibility(View.GONE);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new FavoritesPresenter(new ResourceHelper(this));
        mPresenter.bind(this);
        mPresenter.getVacancies();
    }

    @Override
    public void onGetVacancies(ArrayList<VacancyModel> vacancyModels) {
        if (vacancyModels.isEmpty()) {
            Snackbar.make(recyclerView, getString(R.string.no_data), Snackbar.LENGTH_LONG).show();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new VacanciesAdapter(new ResourceHelper(this), vacancyModels, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCheckBoxClick(boolean isChecked, VacancyModel vacancyModel) {
        if (!isChecked) {
            Snackbar.make(recyclerView, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
            mPresenter.deleteFavoriteVacancy(vacancyModel.getPid());
        }
    }

    @Override
    public void onItemViewClick(int position) {
        Intent browseIntent = new Intent(this, BrowseActivity.class);
        if (mAdapter.getVacancyModels().isEmpty()) {}
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        setResult(AppConstants.REQUEST_CODE,
                new Intent().putParcelableArrayListExtra(getString(R.string.vacancy_list), mAdapter.getVacancyModels()));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unbind();
    }
}
