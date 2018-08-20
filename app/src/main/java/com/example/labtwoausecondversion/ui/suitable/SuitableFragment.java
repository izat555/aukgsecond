package com.example.labtwoausecondversion.ui.suitable;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.IDialogCallback;
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

public class SuitableFragment extends Fragment implements SuitableContract.View, IRecyclerViewClickCallback {
    @BindView(R.id.swipy_refresh_layout)
    SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rotate_loading)
    RotateLoading rotateLoading;
    @BindView(R.id.rl_get_profession)
    RelativeLayout relativeLayout;
    @BindView(R.id.btn_add_prof)
    Button btnAddProf;


    private Unbinder mUnbinder;
    private SuitablePresenter mPresenter;
    private VacanciesAdapter mAdapter;
    private int mPage;
    private IDialogCallback mDialogCallback;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(getString(R.string.my_log), "SuitableFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_vacancies, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        swipyRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipyRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        swipyRefreshLayout.setRefreshing(false);
        mPresenter = new SuitablePresenter(new ResourceHelper(getContext()));
        mDialogCallback = (IDialogCallback) getContext();
        mPresenter.bind(this);
        mPage = 1;
        mPresenter.getVacancies(mPage);
        return view;
    }

    @Override
    public void onGetVacanciesSuccess(List<VacancyModel> vacancyModels) {
        if (relativeLayout.getVisibility() == View.VISIBLE) {
            relativeLayout.setVisibility(View.GONE);
        }

        if (vacancyModels != null && mPage == 1) {
            mAdapter = new VacanciesAdapter(new ResourceHelper(getContext()), vacancyModels, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
        } else if (vacancyModels != null && mPage > 1) {
            mAdapter.addVacancyModels(vacancyModels);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetVacanciesError(String msg) {
        if (msg.isEmpty()) {
            if (relativeLayout.getVisibility() == View.GONE) {
                relativeLayout.setVisibility(View.VISIBLE);
                btnAddProf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogCallback.callDialog(getString(R.string.profession));
                    }
                });
            }
        } else if (!msg.isEmpty()) {
            Snackbar.make(recyclerView, msg, Snackbar.LENGTH_LONG).show();
        }
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

    public void setProfessionFromDialog(String professionName) {
        mPresenter.saveProfessionName(professionName);
        mPage = 1;
        mPresenter.getVacancies(mPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unbind();
    }
}
