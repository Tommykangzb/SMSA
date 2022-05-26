package com.example.campus.model;

import android.util.Log;

import com.example.campus.protoModel.CourseContain.CategoryResult;
import com.example.campus.protoModel.CourseContain.CategoryContainResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CourseModel implements Cloneable{
    private static final String TAG = "CourseModel";
    private int count;
    private boolean isLoadAll;
    private int startIndex;
    private List<CategoryResult> category;

    public CourseModel() {
        count = 0;
        isLoadAll = true;
        startIndex = 0;
        category = new ArrayList<>();
    }


    public <T> void updateCategoryView(Response<T> response) {
        if (response == null || response.body() == null) {
            Log.e(TAG, "response is null");
            return;
        }
        Log.e(TAG, "updateCategoryView");
        CategoryContainResponse responseBody = (CategoryContainResponse) response.body();
        setCount(responseBody.getCount());
        setLoadAll(responseBody.getIsLoadAll());
        setStartIndex(responseBody.getStartIndex());
        setCategory(responseBody.getResultsList());
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CategoryResult> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryResult> category) {
        this.category = category;
    }

    public boolean getIsLoadAll() {
        return isLoadAll;
    }

    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
