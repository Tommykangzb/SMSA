package com.example.campus.model;

import android.util.Log;

import com.example.campus.protoModel.CategoryResponseOuterClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CategoryModel {
    private static final String TAG = "CategoryModel";
    private int count;
    private List<String> category;

    public CategoryModel(){
        count = 0;
        category = new ArrayList<>();
    }



    private <T> void updateCategoryView(Response<T> response){
        if (response == null || response.body() == null){
            Log.e(TAG,"response is null");
            return;
        }
        CategoryResponseOuterClass.CategoryResponse responseBody = (CategoryResponseOuterClass.CategoryResponse) response.body();
        setCount(responseBody.getCount());
        setCategory(responseBody.getCategoryList());
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
