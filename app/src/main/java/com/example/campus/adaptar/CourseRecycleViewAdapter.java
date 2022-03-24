package com.example.campus.adaptar;

import static com.example.campus.view.RecyclerViewHelper.setViewText;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.model.CourseModel;
import com.example.campus.protoModel.CategoryContain.CategoryResult;

import java.util.ArrayList;
import java.util.List;

public class CourseRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CourseAdapter";
    private int mSize = 0;
    private List<CategoryResult> dataList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_adapter_layout, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //ImageView imageView = holder.itemView.findViewById(R.id.chat_contain_avatar);
        //imageView.setImageBitmap();
        setViewText(holder, R.id.contain_subject_name, dataList.get(position).getCourseName());
        setViewText(holder, R.id.contain_subject_grades, dataList.get(position).getCourseGrades());
        setViewText(holder, R.id.contain_subject_teacher, dataList.get(position).getCourseTeacher());
        setViewText(holder, R.id.contain_subject_category, dataList.get(position).getCourseType());
    }

    @Override
    public int getItemCount() {
        return mSize;
    }


    public void updateList(CourseModel courseModel) {
        mSize += courseModel.getCount();
        dataList.addAll(courseModel.getCategory());
        notifyItemRangeChanged(courseModel.getStartIndex(), courseModel.getCount());
        Log.e(TAG, "dataSize: " + dataList.size() + " startIndex: " + courseModel.getStartIndex() + " count: " + courseModel.getCount());
    }

}
