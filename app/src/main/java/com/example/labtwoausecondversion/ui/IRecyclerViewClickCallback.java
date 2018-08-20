package com.example.labtwoausecondversion.ui;

import com.example.labtwoausecondversion.data.entity.VacancyModel;

public interface IRecyclerViewClickCallback {
    void onCheckBoxClick(boolean isChecked, VacancyModel vacancyModel);
    void onItemViewClick(int position);
}
