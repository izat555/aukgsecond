package com.example.labtwoausecondversion.data.network;

import com.example.labtwoausecondversion.data.entity.RubricModel;
import com.example.labtwoausecondversion.data.entity.VacancyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("/mobile-api.php")
    Call<List<VacancyModel>> getVacancies(@Field("login") String login,
                                             @Field("f") String f,
                                             @Field("limit") int limit,
                                             @Field("page") int page);

    @FormUrlEncoded
    @POST("/mobile-api.php")
    Call<List<VacancyModel>> searchVacanciesByProfession(@Field("login") String login,
                                                         @Field("f") String f,
                                                         @Field("limit") int limit,
                                                         @Field("page") int page,
                                                         @Field("search_str") String prof);

    @FormUrlEncoded
    @POST("/mobile-api.php")
    Call<List<RubricModel>> getRubrics(@Field("login") String login,
                                       @Field("f") String f);

    @FormUrlEncoded
    @POST("/mobile-api.php")
    Call<List<VacancyModel>> searchVacancies(@Field("login") String login,
                                             @Field("f") String f,
                                             @Field("limit") int limit,
                                             @Field("page") int page,
                                             @Field("rubric") String rubric,
                                             @Field("salary") String salary,
                                             @Field("term") String term,
                                             @Field("search_str") String prof);
}
