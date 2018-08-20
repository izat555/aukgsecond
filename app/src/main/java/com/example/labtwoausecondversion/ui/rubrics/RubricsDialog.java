package com.example.labtwoausecondversion.ui.rubrics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.ui.IDialogCallback;
import com.example.labtwoausecondversion.ui.IRecyclerViewClickListener;
import com.example.labtwoausecondversion.ui.adapter.CustomRecyclerViewStringAdapter;
import com.example.labtwoausecondversion.ui.adapter.RecyclerViewTouchListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RubricsDialog extends DialogFragment implements IRecyclerViewClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Unbinder mUnbinder;
    private IDialogCallback mDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mDialog = (IDialogCallback) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + " must implement IDialogCallback.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rubrics_dialog, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> rubrics = getArguments().getStringArrayList(getString(R.string.rubric_list_tag));
        if (!rubrics.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new CustomRecyclerViewStringAdapter(getContext(), rubrics));
            recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), recyclerView, this));
        }
    }

    @Override
    public void onClick(View view, int position) {
        TextView tvRubric = view.findViewById(R.id.tv_rubric);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.dialog_tag), getString(R.string.categories_tag));
        bundle.putString(getString(R.string.rubric_tag), tvRubric.getText().toString());
        mDialog.onDialogCallback(bundle);
        dismiss();
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
