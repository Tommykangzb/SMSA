package com.example.campus.view.navigate;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

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


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static HomeFragment fragment;
    private String mFrom;

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
        if(getArguments()!=null){
            mFrom = getArguments().getString("from");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        TextView textView = view.findViewById(R.id.top_school_text);
        textView.setText("SCUT");
        RecyclerView recyclerView = view.findViewById(R.id.recycleView_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(new CategoryRecycleViewAdapter());
        RecyclerView recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerViewContain.setAdapter(new subjectRecycleViewAdapter());
        //ImageView imageView = view.findViewById(R.id.category_selector);
        ISubjectApi subjectApi = RetrofitHelper.getService(ISubjectApi.class);
        //对 发送请求 进行封装
        Call<HelloResponse> call = subjectApi.getTest();
        call.enqueue(new Callback<HelloResponse>() {
            @Override
            public void onResponse(Call<HelloResponse> call, Response<HelloResponse> response) {
            }

            @Override
            public void onFailure(Call<HelloResponse> call, Throwable t) {
            }
        });
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueYellow);
        ScreenHelp.setAndroidNativeLightStatusBar(getActivity(),true);
        return view;
    }
}
