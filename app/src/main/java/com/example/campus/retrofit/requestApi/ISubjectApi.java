package com.example.campus.retrofit.requestApi;

import com.example.campus.model.Test;
import com.example.campus.retrofit.response.HelloResponse;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ISubjectApi {
    @GET("bookDeal/index")
    Call<HelloResponse> getTest();
}
