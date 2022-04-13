package com.example.campus.adaptar;

import static com.example.campus.view.RecyclerViewHelper.setViewText;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.model.CourseModel;
import com.example.campus.protoModel.CategoryContain.CategoryResult;
import com.example.campus.view.Constance;
import com.example.campus.view.course.CourseDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CourseRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private  final String TAG = "CourseAdapter";
    private int mSize = 0;
    private final List<CategoryResult> dataList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_adapter_layout, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String COURSE_NAME = "课程名字：";
        setViewText(holder, R.id.contain_subject_name,
                COURSE_NAME + dataList.get(position).getCourseName());
        String COURSE_GRADE = "评分：";
        setViewText(holder, R.id.contain_subject_grades,
                COURSE_GRADE + dataList.get(position).getCourseGrades());
        String COURSE_TEACHER = "开课教师：";
        setViewText(holder, R.id.contain_subject_teacher,
                COURSE_TEACHER + dataList.get(position).getCourseTeacher());
        String COURSE_TYPE = "课程分类：";
        setViewText(holder, R.id.contain_subject_category,
                COURSE_TYPE + dataList.get(position).getCourseType());
        holder.itemView.setOnClickListener(v ->{
            if (v.getContext() == null){
                return;
            }
            Intent intent = new Intent(v.getContext(), CourseDetailActivity.class);
            intent.putExtra(Constance.KEY_COURSE_NAME,dataList.get(position).getCourseName());
            intent.putExtra(Constance.KEY_COURSE_TEACHER_NAME,dataList.get(position).getCourseTeacher());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mSize;
    }


    public void updateList(CourseModel courseModel) {
        mSize += courseModel.getCount();
        dataList.addAll(courseModel.getCategory());
        notifyItemRangeChanged(courseModel.getStartIndex(), courseModel.getCount());
    }

}
