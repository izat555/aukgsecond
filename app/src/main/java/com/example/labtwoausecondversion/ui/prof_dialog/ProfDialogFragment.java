package com.example.labtwoausecondversion.ui.prof_dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.ui.IDialogCallback;

public class ProfDialogFragment extends DialogFragment {
    private EditText mEtSearch;
    private Button mBtnCancel;
    private Button mBtnSave;
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
        View view = inflater.inflate(R.layout.fragment_prof_dialog, container, false);
        mEtSearch = view.findViewById(R.id.et_search);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnSave = view.findViewById(R.id.btn_save);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                    dismiss();
                    break;
                case R.id.btn_save:
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.dialog_tag), getString(R.string.profession));
                    bundle.putString(getString(R.string.prof_tag), mEtSearch.getText().toString());
                    mDialog.onDialogCallback(bundle);
                    dismiss();
                    break;
            }
        }
    };
}