package com.example.labtwoausecondversion.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.entity.TabItemModel;
import com.example.labtwoausecondversion.ui.IDialogCallback;
import com.example.labtwoausecondversion.ui.adapter.CustomPagerAdapter;
import com.example.labtwoausecondversion.ui.drawer.CustomDrawer;
import com.example.labtwoausecondversion.ui.prof_dialog.ProfDialogFragment;
import com.example.labtwoausecondversion.ui.search.SearchActivity;
import com.example.labtwoausecondversion.ui.suitable.SuitableFragment;
import com.example.labtwoausecondversion.ui.vacancies.VacanciesFragment;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends CustomDrawer implements IDialogCallback {
    @BindView(R.id.custom_toolbar)
    Toolbar customToolbar;
    @BindView(R.id.ib_toolbar)
    ImageButton ibToolbar;
    @BindView(R.id.rotate_loading)
    RotateLoading rotateLoading;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private Unbinder mUnbinder;
    private CustomPagerAdapter mCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(customToolbar);

        initDrawer(this, customToolbar);

        ibToolbar.setImageResource(R.drawable.ic_search_white_30_30);
        ibToolbar.setOnClickListener(mOnClickListener);

        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), getTabItemModels());
        viewPager.setAdapter(mCustomPagerAdapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);

        tabLayout.setupWithViewPager(viewPager);
    }

    private ArrayList<TabItemModel> getTabItemModels() {
        ArrayList<TabItemModel> tabItemModels = new ArrayList<>();
        tabItemModels.add(new TabItemModel(new VacanciesFragment(), getString(R.string.day_vacancies)));
        tabItemModels.add(new TabItemModel(new SuitableFragment(), getString(R.string.suitable_vacancies)));
        return tabItemModels;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                ibToolbar.setImageResource(R.drawable.ic_search_white_30_30);
            } else if (position == 1) {
               ibToolbar.setImageResource(R.drawable.ic_suitable_white_30_30);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton imageButton = (ImageButton) v;
            if (imageButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_search_white_30_30).getConstantState()) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(searchIntent, AppConstants.REQUEST_CODE);
            } else if (imageButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_suitable_white_30_30).getConstantState()) {
                callDialog(getString(R.string.profession));
            }
        }
    };

    @Override
    public void callDialog(String str) {
        if (str.equals(getString(R.string.profession))) {
            ProfDialogFragment profDialogFragment = new ProfDialogFragment();
            profDialogFragment.setCancelable(false);
            profDialogFragment.show(getSupportFragmentManager(), getString(R.string.dialog_tag));
        }
    }

    @Override
    public void onDialogCallback(Bundle bundle) {
        String dialogTag = bundle.getString(getString(R.string.dialog_tag));
        if (dialogTag.equals(getString(R.string.profession))) {
            String prof = bundle.getString(getString(R.string.prof_tag));
            ((SuitableFragment) mCustomPagerAdapter.getItem(1)).setProfessionFromDialog(prof);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
