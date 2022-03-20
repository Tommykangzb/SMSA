package com.example.campus.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus.helper.RetrofitConfig;
import com.example.campus.model.CategoryModel;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.protoModel.CategoryResponseOuterClass.CategoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {
    private static final String TAG = "CategoryViewModel";
    private final MutableLiveData<CategoryModel> categoryModelLiveData = new MutableLiveData<>();

    public CategoryViewModel(){
        loadData();
    }
    public MutableLiveData<CategoryModel> getCategoryModelLiveData() {
        return categoryModelLiveData;
    }

    public void loadData(){
        ApiService subjectApi = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<CategoryResponse> callV2 = subjectApi.getCategoryView();
        callV2.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call,
                                   @NonNull Response<CategoryResponse> response) {
                updateData(response);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "fail " + t);
            }
        });
    }

    public boolean updateSchool(String school){
        return true;
    }

    private <T> void updateData(Response<T> response){
        if (response == null || response.body() == null){
            Log.e(TAG,"response is null");
            return;
        }
        CategoryResponse responseBody = (CategoryResponse) response.body();
        CategoryModel newModel = new CategoryModel();
        newModel.setCount(responseBody.getCount());
        newModel.setCategory(responseBody.getCategoryList());
        categoryModelLiveData.postValue(newModel);
    }

    public static class Factory implements ViewModelProvider.Factory{

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CategoryViewModel();
        }
    }
}
