package com.example.labtwoausecondversion.ui.parameters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.ui.IDialogCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ParametersDialog extends DialogFragment {
    @BindView(R.id.rbggl_job_time)
    RadioButtonGroupGridLayout rbgglJobTime;
    @BindView(R.id.rbggl_salary)
    RadioButtonGroupGridLayout rbgglSalary;
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.btn_ready)
    Button btnReady;

    private Unbinder mUnbinder;
    private IDialogCallback mDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mDialog = (IDialogCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IDialogCallback.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parameters_dialog, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnReset.setOnClickListener(mOnClickListener);
        btnReady.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_reset:
                    rbgglJobTime.reset();
                    rbgglSalary.reset();
                    break;
                case R.id.btn_ready:
                    RadioButton rbJobTime = getView().findViewById(rbgglJobTime.getCheckedRadioButtonId());
                    RadioButton rbSalary = getView().findViewById(rbgglSalary.getCheckedRadioButtonId());
                    String jobTime = rbJobTime.getText().toString();
                    String salary = rbSalary.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.dialog_tag), getString(R.string.parameters_tag));
                    bundle.putString(getString(R.string.job_time_tag), jobTime);
                    bundle.putString(getString(R.string.salary_tag), salary);
                    mDialog.onDialogCallback(bundle);
                    dismiss();
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
