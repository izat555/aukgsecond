package com.example.labtwoausecondversion.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubrubricModel {
    @SerializedName("subrubric_id")
    @Expose
    private String subrubricId;
    @SerializedName("subrubric")
    @Expose
    private String subrubric;
    @SerializedName("count_vac_subrub")
    @Expose
    private String countVacSubrub;
    @SerializedName("count_new_vac_subrub")
    @Expose
    private Object countNewVacSubrub;

    public String getSubrubricId() {
        return subrubricId;
    }

    public void setSubrubricId(String subrubricId) {
        this.subrubricId = subrubricId;
    }

    public String getSubrubric() {
        return subrubric;
    }

    public void setSubrubric(String subrubric) {
        this.subrubric = subrubric;
    }

    public String getCountVacSubrub() {
        return countVacSubrub;
    }

    public void setCountVacSubrub(String countVacSubrub) {
        this.countVacSubrub = countVacSubrub;
    }

    public Object getCountNewVacSubrub() {
        return countNewVacSubrub;
    }

    public void setCountNewVacSubrub(Object countNewVacSubrub) {
        this.countNewVacSubrub = countNewVacSubrub;
    }
}
