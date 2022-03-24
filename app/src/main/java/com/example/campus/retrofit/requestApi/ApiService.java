package com.example.campus.retrofit.requestApi;

import com.example.campus.protoModel.CategoryContain;
import com.example.campus.protoModel.CategoryResponseOuterClass;
import com.example.campus.retrofit.response.HelloResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {
    @GET("bookDeal/index")
    Call<HelloResponse> getTest();

    @GET("bookDeal/index")
    Call<CategoryResponseOuterClass.CategoryResponse> getCategoryView();

    @POST("bookDeal/courseContain")
    Call<CategoryContain.CategoryContainResponse> getCourseContain(@Body CategoryContain.CategoryContainRequest categoryContainRequest);
}