package com.example.campus.view.navigate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.*;
import android.widget.TextView;

import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.adaptar.CategoryRecycleViewAdapter;
import com.example.campus.adaptar.subjectRecycleViewAdapter;
import com.example.campus.retrofit.requestApi.ApiService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.campus.viewModel.CategoryViewModel;
import com.example.campus.protoModel.CategoryResponseOuterClass.CategoryResponse;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static HomeFragment fragment;
    private String mFrom;

    private CategoryRecycleViewAdapter categoryRecycleViewAdapter;
    private subjectRecycleViewAdapter subjectRecycleViewAdapter;
    private TextView schoolTextView;
    private CategoryViewModel categoryViewModel;

    public static HomeFragment newInstance(String from) {
        if (fragment == null) {
            fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("from", from);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
        if (categoryRecycleViewAdapter == null){
            categoryRecycleViewAdapter = new CategoryRecycleViewAdapter();
        }
        if (subjectRecycleViewAdapter == null){
            subjectRecycleViewAdapter = new subjectRecycleViewAdapter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        initView(view);
        setStatusBar();
//        if (categoryViewModel == null){
//            categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
//        }
//        categoryViewModel.getCategoryModelLiveData().observe(this, categoryModel -> {
//            updateCategoryView(categoryModel.getCount(),categoryModel.getCategory());
//        });
        return view;
    }

    private void initView(View view){
        schoolTextView = view.findViewById(R.id.top_school_text);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(categoryRecycleViewAdapter);

        RecyclerView recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewContain.setAdapter(subjectRecycleViewAdapter);
        updateCategoryView();
    }

    private void setStatusBar(){
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueYellow);
        ScreenHelp.setAndroidNativeLightStatusBar(getActivity(), true);
    }

    private void updateCategoryView() {
        ApiService subjectApi = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<CategoryResponse> callOfCategoryView = subjectApi.getCategoryView();
        //异步请求
        callOfCategoryView.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call,
                                   @NonNull Response<CategoryResponse> response) {
                if (response.body() == null) {
                    Log.e(TAG, "response is null");

                    return;
                }
                CategoryResponse categoryResponse = (CategoryResponse) response.body();
                schoolTextView.setText(categoryResponse.getCurrentSchool());
                Log.e(TAG, "count: " + categoryResponse.getCount());
                categoryRecycleViewAdapter.updateData(categoryResponse.getCount(), categoryResponse.getCategoryList());
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "fail " + t);
            }
        });
    }
}
