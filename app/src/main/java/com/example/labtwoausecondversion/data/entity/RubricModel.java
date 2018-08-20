package com.example.labtwoausecondversion.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RubricModel {
    @SerializedName("rubric")
    @Expose
    private String rubric;
    @SerializedName("rubric_id")
    @Expose
    private String rubricId;
    @SerializedName("count_of_vac")
    @Expose
    private String countOfVac;
    @SerializedName("count_of_new_vac")
    @Expose
    private Object countOfNewVac;
    @SerializedName("subrubrics")
    @Expose
    private List<SubrubricModel> subrubrics = null;

    public String getRubric() {
        return rubric;
    }

    public void setRubric(String rubric) {
        this.rubric = rubric;
    }

    public String getRubricId() {
        return rubricId;
    }

    public void setRubricId(String rubricId) {
        this.rubricId = rubricId;
    }

    public String getCountOfVac() {
        return countOfVac;
    }

    public void setCountOfVac(String countOfVac) {
        this.countOfVac = countOfVac;
    }

    public Object getCountOfNewVac() {
        return countOfNewVac;
    }

    public void setCountOfNewVac(Object countOfNewVac) {
        this.countOfNewVac = countOfNewVac;
    }

    public List<SubrubricModel> getSubrubrics() {
        return subrubrics;
    }

    public void setSubrubrics(List<SubrubricModel> subrubrics) {
        this.subrubrics = subrubrics;
    }
}
