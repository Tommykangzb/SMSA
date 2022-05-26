package com.example.campus.view.course;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.adaptar.CategoryRecycleViewAdapter;
import com.example.campus.adaptar.CourseRecycleViewAdapter;
import com.example.campus.retrofit.requestApi.ApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.campus.view.Constance;
import com.example.campus.viewModel.CourseViewModel;
import com.example.campus.protoModel.CategoryResponseOuterClass.CategoryResponse;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String[] schools = {"清华大学", "北京大学", "中山大学", "华南理工大学", "上海交通大学", "浙江大学", "南京大学", "复旦大学"};
    private static HomeFragment fragment;
    private CategoryRecycleViewAdapter categoryRecycleViewAdapter;
    private CourseRecycleViewAdapter courseRecycleViewAdapter;
    private RecyclerView recyclerViewContain;
    private RecyclerView recyclerViewCategory;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private TextView schoolTextView;
    private CourseViewModel courseViewModel;
    private boolean isCourseLoadAll = true;
    private boolean isLoadCourseData = false;

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
        initRecyclerViewAdapter();
        updateCategoryView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        initView(view);
        setStatusBar();
        return view;
    }

    private void initView(View view) {
        spinner = view.findViewById(R.id.top_school_selector);
        spinner.setGravity(Gravity.CENTER_HORIZONTAL);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.layout_school_selector, android.R.id.text1, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //SchoolSelectorAdapter adapter = new SchoolSelectorAdapter(Arrays.asList(schools),getContext());
        spinner.setAdapter(adapter);
        adapter.addAll(Arrays.asList(schools));

        //课程分类
        recyclerViewCategory = view.findViewById(R.id.recycleView_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(categoryRecycleViewAdapter);

        //课程内容
        recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewContain.setAdapter(courseRecycleViewAdapter);
        recyclerViewContain.addOnScrollListener(mContentScrollListener);

        view.findViewById(R.id.imageView_create_sub).setOnClickListener(clickListenerAddItem);
        initCourseViewModel();
    }

    private void setStatusBar() {
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
                //schoolTextView.setText(categoryResponse.getCurrentSchool());
                if (!TextUtils.isEmpty(categoryResponse.getCurrentSchool())){
                    spinner.setSelection(2);
                }
                categoryRecycleViewAdapter.updateData(categoryResponse.getCount(), categoryResponse.getCategoryList());
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), R.string.request_net_error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "fail " + t);
            }
        });
    }

    private void initCourseViewModel() {
        if (courseViewModel != null){
            return;
        }
        courseViewModel = ViewModelProviders.of(this, new CourseViewModel.Factory()).get(CourseViewModel.class);
        courseViewModel.getCategoryModelLiveData().observe(this, courseModel -> {
            if (courseModel == null) {
                Log.e(TAG, "courseModel is null");
                return;
            }
            isCourseLoadAll = courseModel.getIsLoadAll();
            courseRecycleViewAdapter.updateList(courseModel);
            isLoadCourseData = false;
        });

        //服务器已经返回了所有数据
        courseViewModel.getIsLoadAll().observe(this, isLoadAll -> isLoadCourseData = false);
        if (isLoadCourseData) {
            return;
        }
        courseViewModel.loadData(new CourseViewModel.RequestBuilder(CourseViewModel.defaultType, 10, 0, true));
        isLoadCourseData = true;
    }

    private void initRecyclerViewAdapter() {
        if (categoryRecycleViewAdapter == null) {
            categoryRecycleViewAdapter = new CategoryRecycleViewAdapter();
        }
        if (courseRecycleViewAdapter == null) {
            courseRecycleViewAdapter = new CourseRecycleViewAdapter();
        }
    }

    private final RecyclerView.OnScrollListener mContentScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (isLoadCourseData) {
                    return;
                }
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager == null) {
                    return;
                }
                int lastItemPosition = manager.findLastVisibleItemPosition();
                int itemCount = manager.getItemCount();
                if (lastItemPosition == itemCount - 1) {
                    courseViewModel.loadData(new CourseViewModel.RequestBuilder(CourseViewModel.defaultType, 10, 0));
                    isLoadCourseData = true;
                    Log.e(TAG, "scroll bottom, itemCount: " + itemCount);
                }
                Log.e(TAG, "scroll SCROLL_STATE_IDLE");
            }
        }

    };

    private final View.OnClickListener clickListenerAddItem = v -> {
        CommentDialog commentDialog = new CommentDialog(getArguments(), Constance.KEY_DIALOG_TYPE_ADD_CATEGORY);
        commentDialog.showDialog(getActivity());
        commentDialog.addAdapterItem(Arrays.asList(schools), 1);
    };

}
