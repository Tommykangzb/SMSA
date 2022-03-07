package com.example.campus.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int mSize = 7;
    private List<String> dataList = new ArrayList<>(Arrays.asList("热门", "人文", "社科", "计算机", "医学", "材料","物理"));

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter_layout,parent,false);
        return new RecyclerView.ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View v = holder.itemView.findViewById(R.id.category_adapter_textView);
        if (v instanceof TextView){
            ((TextView) v).setText(dataList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mSize;
    }
}
