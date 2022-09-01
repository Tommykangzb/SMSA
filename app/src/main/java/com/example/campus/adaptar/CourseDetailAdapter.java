package com.example.campus.adaptar;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus.API.IBitmapLoadAdapter;
import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.CourseDetail;
import com.google.android.material.imageview.ShapeableImageView;


import static com.example.campus.view.ImageHelper.setViewText;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IBitmapLoadAdapter {
    private static final String TAG = "CourseDetailAdapter";
    private int mSize;
    public Bitmap avatar;
    private final List<CourseDetail.CourseDetailResponseItem> dataList = new ArrayList<>();
    private final List<Integer> likeCountList = new ArrayList<>();
    private final List<Boolean> isSelect = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_details_item_adapter, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            ShapeableImageView avatarImage = holder.itemView.findViewById(R.id.course_detail_item_avatar);
            Glide.with(holder.itemView.getContext())
                    .load(RetrofitConfig.avatarHost + dataList.get(position).getAvatarUrl())
                    .placeholder(R.drawable.image_unload)
                    .into(avatarImage);
            setViewText(holder, R.id.course_detail_item_name, dataList.get(position).getEvaluatorName());
            setViewText(holder, R.id.course_detail_frequency, "考勤频率：" + dataList.get(position).getAttendanceFrequency());
            setViewText(holder, R.id.course_detail_attendance_way, "考勤方式：" + dataList.get(position).getAttendanceWay());
            setViewText(holder, R.id.course_detail_exam_way, "考核方式：" + dataList.get(position).getExamWay());
            setViewText(holder, R.id.course_detail_exam_given, "给分情况：" + dataList.get(position).getExamGivenGrades());
            setViewText(holder, R.id.course_detail_course_score, "评分：" + dataList.get(position).getCourseScore());
            setViewText(holder, R.id.course_detail_credit, "学分：" + dataList.get(position).getCredit());
            setViewText(holder, R.id.course_evaluate_words, "整体评价： " + dataList.get(position).getCourseEvaluateWords());
            if (TextUtils.isEmpty(dataList.get(position).getDate())){
                setViewText(holder, R.id.course_detail_date, "刚刚！");
            } else {
                setViewText(holder, R.id.course_detail_date, dataList.get(position).getDate());
            }
            Log.e(TAG, "2: position: " + position + "  likeSize: " + likeCountList.size());
            if (position >= likeCountList.size()) {
                likeCountList.add(dataList.get(position).getLikeCount());
                isSelect.add(false);
            }
            setViewText(holder, R.id.course_detail_like, String.valueOf(likeCountList.get(position)));
            holder.itemView.findViewById(R.id.course_detail_like_image).setOnClickListener(v -> {
                boolean flag = isSelect.get(position);
                int like = likeCountList.get(position);
                likeCountList.set(position, flag ? like - 1 : like + 1);
                v.setSelected(!flag);
                isSelect.set(position, !flag);
                notifyItemChanged(position, 1);
            });
        } else {
            int type = (int) payloads.get(0);
            if (type == 1) {
                setViewText(holder, R.id.course_detail_like, String.valueOf(likeCountList.get(position)));
                holder.itemView.findViewById(R.id.course_detail_like_image).setOnClickListener(v -> {
                    boolean flag = isSelect.get(position);
                    int like = likeCountList.get(position);
                    likeCountList.set(position, flag ? like - 1 : like + 1);
                    v.setSelected(!flag);
                    isSelect.set(position, !flag);
                    notifyItemChanged(position, 1);
                });
            }
        }
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

    public void addList(CourseDetail.CourseDetailResponseItem item) {
        int startIndex = mSize;
        mSize += 1;
        dataList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.avatar = bitmap;
    }
}
