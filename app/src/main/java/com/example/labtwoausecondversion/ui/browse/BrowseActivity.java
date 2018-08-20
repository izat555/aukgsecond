package com.example.labtwoausecondversion.ui.browse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.data.SQLiteHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BrowseActivity extends AppCompatActivity {
    @BindView(R.id.custom_toolbar)
    Toolbar customToolbar;
    @BindView(R.id.ll_prev)
    LinearLayout linearLayoutPrev;
    @BindView(R.id.ll_next)
    LinearLayout linearLayoutNext;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_salary)
    TextView tvSalary;
    @BindView(R.id.tv_site)
    TextView tvSite;
    @BindView(R.id.tv_telephone)
    TextView tvTelephone;
    @BindView(R.id.cb_favorites)
    CheckBox cbFavorites;
    @BindView(R.id.tv_body)
    TextView tvBody;
    @BindView(R.id.btn_call)
    Button btnCall;

    private Unbinder mUnbinder;
    private ArrayList<VacancyModel> mVacancyModels;
    private int mPosition;
    private SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mUnbinder = ButterKnife.bind(this);

        customToolbar.setTitle(getString(R.string.title_vacancies));
        customToolbar.findViewById(R.id.ib_toolbar).setVisibility(View.GONE);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvBody.setMovementMethod(new ScrollingMovementMethod());

        linearLayoutPrev.setOnClickListener(mOnClickListener);
        linearLayoutNext.setOnClickListener(mOnClickListener);
        cbFavorites.setOnClickListener(mOnClickListener);
        btnCall.setOnClickListener(mOnClickListener);

        mVacancyModels = getIntent().getParcelableArrayListExtra(getString(R.string.vacancy_list));
        mPosition = getIntent().getIntExtra(getString(R.string.position), 0);
        mSQLiteHelper = new SQLiteHelper(this);

        setValues();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_prev:
                    mPosition--;
                    setValues();
                    break;
                case R.id.ll_next:
                    mPosition++;
                    setValues();
                    break;
                case R.id.cb_favorites:
                    VacancyModel vacancyModel = mVacancyModels.get(mPosition);
                    if (vacancyModel.isChecked()) {
                        vacancyModel.setChecked(false);
                        ((CheckBox) v).setButtonDrawable(R.drawable.iv_fav_default_45_45);
                        mSQLiteHelper.deleteSingleVacancy(vacancyModel.getPid());
                        Snackbar.make(btnCall, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
                    } else {
                        vacancyModel.setChecked(true);
                        ((CheckBox) v).setButtonDrawable(R.drawable.iv_fav_45_45);
                        mSQLiteHelper.saveSingleVacancy(vacancyModel);
                        Snackbar.make(btnCall, R.string.add_to_fav, Snackbar.LENGTH_LONG).show();
                    }
                    break;
                case R.id.btn_call:
                    String phone = mVacancyModels.get(mPosition).getTelephone();
                    Uri number = Uri.parse("tel:" + phone);
                    startActivity(new Intent(Intent.ACTION_DIAL, number));
                    break;
            }
        }
    };

    private void setValues() {
        if (mVacancyModels != null) {
            if (mPosition == 0 && mVacancyModels.size() > 1) {
                linearLayoutPrev.setVisibility(View.INVISIBLE);
                linearLayoutNext.setVisibility(View.VISIBLE);
            } else if (mPosition == mVacancyModels.size() - 1 && mVacancyModels.size() > 1) {
                linearLayoutNext.setVisibility(View.INVISIBLE);
                linearLayoutPrev.setVisibility(View.VISIBLE);
            } else if (mVacancyModels.size() > 1) {
                linearLayoutPrev.setVisibility(View.VISIBLE);
                linearLayoutNext.setVisibility(View.VISIBLE);
            }
        }

        VacancyModel vacancyModel = mVacancyModels.get(mPosition);

        tvHeader.setText(vacancyModel.getHeader());
        tvProfile.setText(vacancyModel.getProfile());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(vacancyModel.getUpdateDate());
            tvDate.setText(new SimpleDateFormat("HH:mm dd MMM yyyy").format(new Date(date.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvSalary.setText(vacancyModel.getSalary());
        tvSite.setText(vacancyModel.getSiteAddress());
        tvTelephone.setText(vacancyModel.getTelephone());

        if (vacancyModel.isChecked()) {
            cbFavorites.setChecked(true);
            cbFavorites.setButtonDrawable(R.drawable.iv_fav_45_45);
        } else {
            cbFavorites.setChecked(false);
            cbFavorites.setButtonDrawable(R.drawable.iv_fav_default_45_45);
        }

        tvBody.setText(vacancyModel.getBody());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(getString(R.string.vacancy_list), mVacancyModels);
        setResult(AppConstants.REQUEST_CODE, intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
