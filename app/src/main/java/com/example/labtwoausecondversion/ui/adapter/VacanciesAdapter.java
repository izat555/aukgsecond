package com.example.labtwoausecondversion.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.data.ResourceHelper;
import com.example.labtwoausecondversion.data.entity.VacancyModel;
import com.example.labtwoausecondversion.ui.IRecyclerViewClickCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VacanciesAdapter extends RecyclerView.Adapter<VacanciesAdapter.VacancyHolder> {
    private List<VacancyModel> mVacancyModels;
    private ResourceHelper mResourceHelper;
    private IRecyclerViewClickCallback mRecyclerViewClickCallback;

    class VacancyHolder extends RecyclerView.ViewHolder {
        private TextView mTvProfession;
        private TextView mTvDate;
        private TextView mTvVacancyDescription;
        private TextView mTvVacancySalary;
        private CheckBox mCbFavorites;

        public VacancyHolder(View itemView) {
            super(itemView);
            mTvProfession = itemView.findViewById(R.id.tv_profession);
            mTvDate = itemView.findViewById(R.id.tv_date);
            mTvVacancyDescription = itemView.findViewById(R.id.tv_description);
            mTvVacancySalary = itemView.findViewById(R.id.tv_salary);
            mCbFavorites = itemView.findViewById(R.id.cb_favorites);
            mCbFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VacancyModel vacancyModel = mVacancyModels.get(getAdapterPosition());
                    if (vacancyModel.isChecked()) {
                        vacancyModel.setChecked(false);
                    } else {
                        vacancyModel.setChecked(true);
                    }
                    notifyDataSetChanged();
                    mRecyclerViewClickCallback.onCheckBoxClick(vacancyModel.isChecked(), vacancyModel);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerViewClickCallback.onItemViewClick(getAdapterPosition());
                }
            });
        }
    }

    public VacanciesAdapter(ResourceHelper resourceHelper, List<VacancyModel> vacancyModels, IRecyclerViewClickCallback recyclerViewClickCallback) {
        mResourceHelper = resourceHelper;
        mRecyclerViewClickCallback = recyclerViewClickCallback;
        mVacancyModels = vacancyModels;
    }

    @NonNull
    @Override
    public VacancyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        VacancyHolder vacancyHolder = new VacancyHolder(view);
        return vacancyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VacancyHolder holder, int position) {
        holder.mTvProfession.setText(mVacancyModels.get(position).getProfession());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(mVacancyModels.get(position).getUpdateDate());
            holder.mTvDate.setText(new SimpleDateFormat("HH:mm dd MMM yyyy")
                    .format(new Date(date.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mTvVacancyDescription.setText(mVacancyModels.get(position).getBody());

        String salary = mVacancyModels.get(position).getSalary();
        if (mResourceHelper != null) {
            holder.mTvVacancySalary
                    .setText(salary.isEmpty() ? mResourceHelper.getContext().getString(R.string.negotiation) : salary);
        }

        if (mVacancyModels.get(position).isChecked()) {
            holder.mCbFavorites.setButtonDrawable(R.drawable.iv_fav_45_45);
            holder.mCbFavorites.setChecked(true);
        } else {
            holder.mCbFavorites.setButtonDrawable(R.drawable.iv_fav_default_45_45);
            holder.mCbFavorites.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mVacancyModels.size();
    }

    public void addVacancyModels(List<VacancyModel> vacancyModels) {
        mVacancyModels.addAll(vacancyModels);
    }

    public void changeVacancyModelsState(List<VacancyModel> vacancyModels) {
        if (vacancyModels.isEmpty()) {
            return;
        }

        for (int i = 0; i < mVacancyModels.size(); i++) {
            for (VacancyModel model : vacancyModels) {
                if (mVacancyModels.get(i).getPid().equals(model.getPid())) {
                    mVacancyModels.get(i).setChecked(model.isChecked());
                }
            }
        }

        notifyDataSetChanged();
    }

    public ArrayList<VacancyModel> getVacancyModels() {
        return (ArrayList<VacancyModel>) mVacancyModels;
    }
}
