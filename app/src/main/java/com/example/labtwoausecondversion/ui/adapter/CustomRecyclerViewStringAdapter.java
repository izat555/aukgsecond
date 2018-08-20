package com.example.labtwoausecondversion.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.labtwoausecondversion.R;

import java.util.ArrayList;

public class CustomRecyclerViewStringAdapter extends RecyclerView.Adapter<CustomRecyclerViewStringAdapter.CustomHolder> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mRubrics;

    class CustomHolder extends RecyclerView.ViewHolder {
        TextView tvRubric;

        public CustomHolder(View itemView) {
            super(itemView);
            tvRubric = itemView.findViewById(R.id.tv_rubric);
        }
    }

    public CustomRecyclerViewStringAdapter(Context context, ArrayList<String> rubrics) {
        mLayoutInflater = LayoutInflater.from(context);
        mRubrics = rubrics;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.custom_string_row, parent, false);
        CustomHolder customHolder = new CustomHolder(view);
        return customHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.tvRubric.setText(mRubrics.get(position));
    }

    @Override
    public int getItemCount() {
        return mRubrics.size();
    }
}
