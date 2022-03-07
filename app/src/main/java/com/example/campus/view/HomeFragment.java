package com.example.campus.view;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campus.R;
import com.example.campus.model.CategoryRecycleViewAdapter;
import com.example.campus.model.subjectRecycleViewAdapter;


public class HomeFragment extends Fragment {
    private String mFrom;
   public static HomeFragment newInstance(String from){
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
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

        return view;
    }
}
