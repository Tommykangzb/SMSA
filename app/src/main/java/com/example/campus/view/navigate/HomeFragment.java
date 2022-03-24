package com.example.campus.view.navigate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.adaptar.CategoryRecycleViewAdapter;
import com.example.campus.adaptar.CourseRecycleViewAdapter;
import com.example.campus.retrofit.requestApi.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.campus.viewModel.CourseViewModel;
import com.example.campus.protoModel.CategoryResponseOuterClass.CategoryResponse;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String []schools = {"清华大学","北京大学","中山大学","华南理工大学","上海交通大学","浙江大学","南京大学","复旦大学"};
    private static final String []school = {" "};
    private static HomeFragment fragment;
    private String mFrom;

    private CategoryRecycleViewAdapter categoryRecycleViewAdapter;
    private CourseRecycleViewAdapter courseRecycleViewAdapter;
    private TextView schoolTextView;
    private CourseViewModel courseViewModel;
    private boolean isCourseLoadAll = true;
    private boolean isLoadCourseData= false;

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
        if (courseRecycleViewAdapter == null){
            courseRecycleViewAdapter = new CourseRecycleViewAdapter();
        }
        courseViewModel = ViewModelProviders.of(this, new CourseViewModel.Factory()).get(CourseViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        initView(view);
        setStatusBar();
        return view;
    }

    private void initView(View view){
        Spinner spinner = view.findViewById(R.id.top_school_selector);
        spinner.setGravity(Gravity.CENTER_HORIZONTAL);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.layout_school_selector, android.R.id.text1, school);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        //schoolTextView = view.findViewById(R.id.top_school_text);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(categoryRecycleViewAdapter);

        RecyclerView recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewContain.setAdapter(courseRecycleViewAdapter);
        recyclerViewContain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
                if (isLoadCourseData){
                    return;
                }
                LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (manager == null){
                    return;
                }
                int lastItemPosition = manager.findLastVisibleItemPosition();
                int itemCount = manager.getItemCount();
                if (lastItemPosition == itemCount-1){
                    courseViewModel.loadData(new CourseViewModel.RequestBuilder(CourseViewModel.defaultType, 10, 0));
                    isLoadCourseData = true;
                    Log.e(TAG, "scroll bottom, itemCount: " + itemCount);
                }
            }
        });
        updateCategoryView();
        initCourseViewModel();
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
                CategoryResponse categoryResponse = response.body();
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

    private void initCourseViewModel() {
        courseViewModel.getCategoryModelLiveData().observe(this, courseModel -> {
            if (courseModel == null){
                Log.e(TAG,"courseModel is null");
                return;
            }
            isCourseLoadAll = courseModel.getIsLoadAll();
            courseRecycleViewAdapter.updateList(courseModel);
            isLoadCourseData = false;
        });
        courseViewModel.getIsLoadAll().observe(this, isLoadAll -> {

        });
        if (isLoadCourseData){
            return;
        }
        courseViewModel.loadData(new CourseViewModel.RequestBuilder(CourseViewModel.defaultType, 10, 0));
        isLoadCourseData = true;
    }
}
