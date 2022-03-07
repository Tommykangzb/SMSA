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

public class subjectRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int mSize = 7;
    private List<String> dataList = new ArrayList<>(Arrays.asList("酒文化鉴赏与啤酒的酿造工艺讲解", "现代小说选集", "水利工程概论", "计算机概论", "医学解剖", "材料力学基础","天体物理"));
    private List<String> dataListGrades = new ArrayList<>(Arrays.asList("95", "93", "92", "96", "90", "85","86"));
    private List<String> dataListTeacher = new ArrayList<>(Arrays.asList("刘墉", "方猪猪", "海盗狗", "海盗猪", "Aadsa", "Ddidae","Mhala"));
    private List<String> dataListCategory = new ArrayList<>(Arrays.asList("人文核心", "人文", "社科", "计算机", "材料", "医学","物理"));
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_adapter_layout,parent,false);
        return new RecyclerView.ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View viewName = holder.itemView.findViewById(R.id.contain_subject_name);
        if (viewName instanceof TextView){
            ((TextView) viewName).setText(dataList.get(position));
        }
        View viewGrades = holder.itemView.findViewById(R.id.contain_subject_grades);
        if (viewGrades instanceof TextView){
            ((TextView) viewGrades).setText(dataListGrades.get(position));
        }
        View viewTeacher = holder.itemView.findViewById(R.id.contain_subject_teacher);
        if (viewTeacher instanceof TextView){
            ((TextView) viewTeacher).setText(dataListTeacher.get(position));
        }
        View viewCategory = holder.itemView.findViewById(R.id.contain_subject_category);
        if (viewCategory instanceof TextView){
            ((TextView) viewCategory).setText(dataListCategory.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mSize;
    }
}
