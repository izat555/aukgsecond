package com.example.labtwoausecondversion.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.ui.IDialogCallback;
import com.example.labtwoausecondversion.ui.parameters.ParametersDialog;
import com.example.labtwoausecondversion.ui.rubrics.RubricsDialog;
import com.example.labtwoausecondversion.ui.search_result.SearchResultActivity;
import com.victor.loading.rotate.RotateLoading;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchActivity extends AppCompatActivity implements IDialogCallback, SearchContract.View {
    @BindView(R.id.custom_toolbar)
    Toolbar customToolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ib_search)
    ImageButton ibSearch;
    @BindView(R.id.rl_parameters)
    RelativeLayout rlParameters;
    @BindView(R.id.ib_parameters)
    ImageButton ibParameters;
    @BindView(R.id.tv_chosen_job_time)
    TextView tvChosenJobTime;
    @BindView(R.id.tv_chosen_salary)
    TextView tvChosenSalary;
    @BindView(R.id.rl_categories)
    RelativeLayout rlCategories;
    @BindView(R.id.ib_categories)
    ImageButton ibCategories;
    @BindView(R.id.tv_chosen_category)
    TextView tvChosenCategory;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.rotate_loading)
    RotateLoading rotateLoading;

    private Unbinder mUnbinder;
    private SearchPresenter mPresenter;
    private ArrayList<String> mCategoriesList;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mUnbinder = ButterKnife.bind(this);

        customToolbar.findViewById(R.id.ib_toolbar).setVisibility(View.GONE);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setTitle(getString(R.string.search));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etSearch.addTextChangedListener(mTextWatcher);
        ibSearch.setOnClickListener(mOnClickListener);
        rlParameters.setOnClickListener(mOnClickListener);
        ibParameters.setOnClickListener(mOnClickListener);
        rlCategories.setOnClickListener(mOnClickListener);
        ibCategories.setOnClickListener(mOnClickListener);
        btnSearch.setOnClickListener(mOnClickListener);

        mPresenter = new SearchPresenter(new ResourceHelper(this));
        mPresenter.bind(this);
        mCategoriesList = new ArrayList<>();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()) {
                ibSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_black_opacity_70_24dp));
            } else {
                ibSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_black_opacity_70_24dp));
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_search:
                    etSearch.setText("");
                    break;
                case R.id.rl_parameters:
                    callDialog(getString(R.string.parameters_tag));
                    break;
                case R.id.rl_categories:
                    if (mCategoriesList.isEmpty()) {
                        mPresenter.getCategories();
                    } else if (!mCategoriesList.isEmpty()) {
                        callDialog(getString(R.string.categories_tag));
                    }
                    break;
                case R.id.ib_parameters:
                    ImageButton imageParameters = (ImageButton) v;
                    if (imageParameters.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_close_red_24dp).getConstantState()) {
                        tvChosenJobTime.setVisibility(View.GONE);
                        tvChosenSalary.setVisibility(View.GONE);
                        imageParameters.setImageResource(R.drawable.ic_chevron_right_black_opacity_70_24dp);
                    } else if (imageParameters.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_chevron_right_black_opacity_70_24dp).getConstantState()) {
                        callDialog(getString(R.string.parameters_tag));
                    }
                    break;
                case R.id.ib_categories:
                    ImageButton imageCategories = (ImageButton) v;
                    if (imageCategories.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_close_red_24dp).getConstantState()) {
                        tvChosenCategory.setVisibility(View.GONE);
                        imageCategories.setImageResource(R.drawable.ic_chevron_right_black_opacity_70_24dp);
                    } else if (imageCategories.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_chevron_right_black_opacity_70_24dp).getConstantState()) {
                        if (mCategoriesList.isEmpty()) {
                            mPresenter.getCategories();
                        } else if (!mCategoriesList.isEmpty()) {
                            callDialog(getString(R.string.categories_tag));
                        }
                    }
                    break;
                case R.id.btn_search:
                    if (TextUtils.isEmpty(etSearch.getText().toString())) {
                        Snackbar.make(btnSearch, "Enter something", Snackbar.LENGTH_LONG).show();
                    } else if (!TextUtils.isEmpty(etSearch.getText().toString())){
                        String search = etSearch.getText().toString();
                        String jobTime = tvChosenJobTime.getText().toString();
                        String salary = tvChosenSalary.getText().toString();
                        String rubric = tvChosenCategory.getText().toString();
                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra(getString(R.string.prof_tag), search);
                        intent.putExtra(getString(R.string.rubric_tag), rubric);
                        intent.putExtra(getString(R.string.salary_tag), salary);
                        intent.putExtra(getString(R.string.term_tag), jobTime);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    public void callDialog(String dialogTag) {
        if (dialogTag.equals(getString(R.string.parameters_tag))) {
            ParametersDialog parametersDialog = new ParametersDialog();
            parametersDialog.setCancelable(false);
            parametersDialog.show(getSupportFragmentManager(), dialogTag);
        } else if (dialogTag.equals(getString(R.string.categories_tag))) {
            RubricsDialog rubricsDialog = new RubricsDialog();
            rubricsDialog.setCancelable(false);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(getString(R.string.rubric_list_tag), mCategoriesList);
            rubricsDialog.setArguments(bundle);
            rubricsDialog.show(getSupportFragmentManager(), getString(R.string.categories_tag));
        }
    }

    @Override
    public void onDialogCallback(Bundle bundle) {
        if (bundle != null) {
            String dialogTag = bundle.getString(getString(R.string.dialog_tag));
            if (dialogTag.equals(getString(R.string.parameters_tag))) {
                String jobTime = bundle.getString(getString(R.string.job_time_tag));
                String salary = bundle.getString(getString(R.string.salary_tag));
                if (!jobTime.isEmpty()) {
                    tvChosenJobTime.setText(jobTime);
                    tvChosenJobTime.setVisibility(View.VISIBLE);
                    ibParameters.setImageResource(R.drawable.ic_close_red_24dp);
                }
                if (!salary.isEmpty()) {
                    tvChosenSalary.setText(salary);
                    tvChosenSalary.setVisibility(View.VISIBLE);
                    ibParameters.setImageResource(R.drawable.ic_close_red_24dp);
                }
            } else if (dialogTag.equals(getString(R.string.categories_tag))) {
                String chosenRubric = bundle.getString(getString(R.string.rubric_tag));
                if (!chosenRubric.isEmpty()) {
                    tvChosenCategory.setText(chosenRubric);
                    tvChosenCategory.setVisibility(View.VISIBLE);
                    ibCategories.setImageResource(R.drawable.ic_close_red_24dp);
                }
            }
        }
    }

    @Override
    public void onGetCategoriesCallback(ArrayList<String> rubrics) {
        mCategoriesList = rubrics;
        callDialog(getString(R.string.categories_tag));
    }

    @Override
    public void onGetCategoriesError(String msg) {
        Snackbar.make(btnSearch, msg, Snackbar.LENGTH_LONG).show();
    }

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
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unbind();
    }
}
