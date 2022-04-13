package com.example.campus.adaptar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.protoModel.CourseDetail;
import com.example.campus.view.RecyclerViewHelper;
import com.google.android.material.imageview.ShapeableImageView;


import static com.example.campus.view.RecyclerViewHelper.setViewText;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CourseDetailAdapter";
    private int mSize = 6;
    private final String[] data = {"小土豆", "偶尔", "随机点名", "论文", "90+", "2", "94", "整体还是不错的！", "2021/4/21", "100"};
    public Bitmap avatar;
    List<CourseDetail.CourseDetailResponseItem> dataList = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_details_item_adapter, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ShapeableImageView avatarImage  = holder.itemView.findViewById(R.id.course_detail_item_avatar);
//        loadBitmapFromURL(url,avatarImage);
//        Log.e(TAG,"after loadImage");
//        setViewText(holder, R.id.course_detail_item_name, dataList.get(position).getEvaluatorName());
//        setViewText(holder, R.id.course_detail_frequency, "考勤频率：" + dataList.get(position).getAttendanceFrequency());
//        setViewText(holder, R.id.course_detail_attendance_way, "考勤方式：" + dataList.get(position).getAttendanceWay());
//        setViewText(holder, R.id.course_detail_exam_way, "考核方式：" + dataList.get(position).getExamWay());
//        setViewText(holder, R.id.course_detail_exam_given, "给分情况：" + dataList.get(position).getExamGivenGrades());
//        setViewText(holder, R.id.course_detail_course_score, "评分：" + dataList.get(position).getCourseScore());
//        setViewText(holder, R.id.course_detail_credit, "学分：" + dataList.get(position).getCredit());
//        setViewText(holder, R.id.course_evaluate_words, "整体评价： " + dataList.get(position).getCourseEvaluateWords());
//        setViewText(holder, R.id.course_detail_date, dataList.get(position).getDate());
//        setViewText(holder, R.id.course_detail_like, String.valueOf(dataList.get(position).getLikeCount()));
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "after loadImage");
        setViewText(holder, R.id.course_detail_item_name, data[0]);
        setViewText(holder, R.id.course_detail_frequency, "考勤频率：" + data[1]);
        setViewText(holder, R.id.course_detail_attendance_way, "考勤方式：" + data[2]);
        setViewText(holder, R.id.course_detail_exam_way, "考核方式：" + data[3]);
        setViewText(holder, R.id.course_detail_exam_given, "给分情况：" + data[4]);
        setViewText(holder, R.id.course_detail_credit, "学分：" + data[5]);
        setViewText(holder, R.id.course_detail_course_score, "评分：" + data[6]);
        setViewText(holder, R.id.course_evaluate_words, "整体评价： " + data[7]);
        setViewText(holder, R.id.course_detail_date, data[8]);
        setViewText(holder, R.id.course_detail_like, data[9]);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull List payloads) {
        if (payloads.isEmpty() || avatar == null) {
            setViewText(holder, R.id.course_detail_item_name, data[0]);
            setViewText(holder, R.id.course_detail_frequency, "考勤频率：" + data[1]);
            setViewText(holder, R.id.course_detail_attendance_way, "考勤方式：" + data[2]);
            setViewText(holder, R.id.course_detail_exam_way, "考核方式：" + data[3]);
            setViewText(holder, R.id.course_detail_exam_given, "给分情况：" + data[4]);
            setViewText(holder, R.id.course_detail_credit, "学分：" + data[5]);
            setViewText(holder, R.id.course_detail_course_score, "评分：" + data[6]);
            setViewText(holder, R.id.course_evaluate_words, "整体评价： " + data[7]);
            setViewText(holder, R.id.course_detail_date, data[8]);
            setViewText(holder, R.id.course_detail_like, data[9]);
            Log.e(TAG, "avatar is null  " + position);
            return;
        }
        //int type = (int) payloads.get(0);
        ShapeableImageView avatarImage = holder.itemView.findViewById(R.id.course_detail_item_avatar);
        avatarImage.setImageBitmap(avatar);
    }


    @Override
    public int getItemCount() {
        return mSize;
    }

    public void updateList(CourseDetail.CourseDetailResponse response) {
        int startIndex = mSize;
        mSize += response.getCourseDetailItemsList().size();
        dataList.addAll(response.getCourseDetailItemsList());
        notifyItemRangeChanged(startIndex, response.getCourseDetailItemsList().size());
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
        //notifyItemChanged(position,0);
    }
}
