package com.example.campus.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus.helper.RetrofitConfig;
import com.example.campus.model.CourseModel;
import com.example.campus.protoModel.CategoryContain;
import com.example.campus.protoModel.CategoryContain.CategoryContainResponse;
import com.example.campus.retrofit.requestApi.ApiService;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseViewModel extends ViewModel {
    private static final String TAG = "CategoryViewModel";
    private final MutableLiveData<CourseModel> categoryModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadAll = new MutableLiveData<>();
    public static final String defaultType = "course_type_all";
    private String currentType = "course_type_all";

    public CourseViewModel() {
        isLoadAll.setValue(false);
        categoryModelLiveData.setValue(new CourseModel());
    }

    public MutableLiveData<CourseModel> getCategoryModelLiveData() {
        return categoryModelLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadAll(){
        return isLoadAll;
    }

    public static class RequestBuilder {
        public int limitCount;
        public int startIndex;
        public String type;

        public RequestBuilder(String type, int limitCount, int startIndex) {
            this.type = type;
            this.limitCount = limitCount;
            this.startIndex = startIndex;
        }
    }

    public void loadData(RequestBuilder requestBuilder){
        CategoryContain.CategoryContainRequest.Builder builder = CategoryContain.CategoryContainRequest.newBuilder();
        builder.setType(requestBuilder.type)
                .setLimitCount(requestBuilder.limitCount)
                .setStartIndex(requestBuilder.startIndex);
        ApiService courseApi = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<CategoryContainResponse> courseRequest = courseApi.getCourseContain(builder.build());
        currentType = requestBuilder.type;
        courseRequest.enqueue(new Callback<CategoryContainResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryContainResponse> call,
                                   @NonNull Response<CategoryContainResponse> response) {
                try {
                    updateData(response);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryContainResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "fail: " + t);
            }
        });
    }

    private <T> void updateData(Response<T> response) throws CloneNotSupportedException {
        if (response == null || response.body() == null) {
            Log.e(TAG, "response is null");
            return;
        }
        boolean loadAll = ((CategoryContainResponse) response.body()).getIsLoadAll();
        if (loadAll) {
            isLoadAll.postValue(true);
            return;
        }
        Objects.requireNonNull(categoryModelLiveData.getValue()).updateCategoryView(response);
        CourseModel newModel = (CourseModel) categoryModelLiveData.getValue().clone();
        newModel.setCategory(new ArrayList<>(newModel.getCategory()));
        categoryModelLiveData.postValue(newModel);
    }

    public static class Factory implements ViewModelProvider.Factory{
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CourseViewModel();
        }
    }
}
