package com.example.campus.view.navigate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.helper.RetrofitHelper;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.model.CategoryRecycleViewAdapter;
import com.example.campus.model.subjectRecycleViewAdapter;
import com.example.campus.retrofit.requestApi.ISubjectApi;
import com.example.campus.retrofit.response.HelloResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDealFragment extends Fragment {
    private static final String TAG = "BookDealFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //复用课程评价的布局
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        //隐藏学校以及课程分类等View
        view.findViewById(R.id.top_school_text).setVisibility(View.GONE);
        view.findViewById(R.id.category_selector).setVisibility(View.GONE);
        view.findViewById(R.id.top_school_selector).setVisibility(View.GONE);
        view.findViewById(R.id.imageView_create_sub).setVisibility(View.GONE);

        //设置搜索框以及背景
        LinearLayout layout = view.findViewById(R.id.top_message_layout);
        LinearLayout.LayoutParams LParams = new LinearLayout.LayoutParams(layout.getLayoutParams());
        LParams.setMargins(60, 0, 0, 0);
        layout.setLayoutParams(LParams);
        layout.setBackground(getActivity().getDrawable(R.color.white));
        addMenuView(layout);
        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setBackground(getActivity().getDrawable(R.drawable.book_deal_search_bg));
        view.findViewById(R.id.bottom_layout_category).setVisibility(View.GONE);

        //配置专属View
        RecyclerView recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        //recyclerViewContain.setAdapter(new subjectRecycleViewAdapter());
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueWhite);
        ScreenHelp.setAndroidNativeLightStatusBar(getActivity(),true);
        return view;
    }

    public void initView(){

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addMenuView(LinearLayout linearLayout) {
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(80, 0, 0, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(params);
        imageView.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_icon_create_subject));
        linearLayout.addView(imageView);
    }

}
