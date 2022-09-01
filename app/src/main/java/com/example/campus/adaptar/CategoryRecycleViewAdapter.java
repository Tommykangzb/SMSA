package com.example.campus.adaptar;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.protoModel.CourseContain;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private int mSize;
    private int lastSize = 0;
    private List<String> dataList;
    TextView lastToughView;
    private CourseRecycleViewAdapter adapter;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter_layout,parent,false);
        Log.i(TAG, "onCreateViewHolder");
        return new RecyclerView.ViewHolder(view){};
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView v = holder.itemView.findViewById(R.id.category_adapter_textView);
        Log.i(TAG, "onBindViewHolder: " + position);
        if (position == 0 && lastToughView == null) {
            lastToughView = v;
            changeTextFont(v, 24, true);
        }
        if (dataList != null && dataList.size() != 0) {
            v.setText(dataList.get(position));
            v.setOnTouchListener((v12, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (lastToughView == null) {
                        lastToughView = v;
                    } else if (lastToughView != v) {
                        changeTextFont(lastToughView, 22, false);
                        lastToughView = v;
                    }
                    changeTextFont(v, 24, true);
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public void updateData(int count, @NonNull List<String> data) {
        mSize += count;
        dataList = data;
        notifyItemRangeChanged(lastSize, count);
        lastSize += mSize;
        Log.i(TAG, "updateData" + count);
    }

    public List<String> getDataList(){
        return dataList;
    }

    private void changeTextFont(TextView v, int size, boolean bold) {
        v.setTextSize(size);
        v.getPaint().setFakeBoldText(bold);
        if (adapter == null || !bold){
            return;
        }
        List<CourseContain.CategoryResult> list = adapter.getOriginDataList();
        List<CourseContain.CategoryResult> newList = new ArrayList<>();
        String str = v.getText().toString();
        if (TextUtils.isEmpty(str)){
            return;
        }
        if (str.equals("热门")){
            adapter.updateList(new ArrayList<>());
            return;
        }
        for (CourseContain.CategoryResult item: list) {
            if (str.equals(item.getCourseType())){
                newList.add(item);
            }
        }

        if (newList.size() != 0){
            adapter.updateList(newList);
        }
    }
    public void setAdapter(CourseRecycleViewAdapter adapter){
        this.adapter = adapter;
    }

}
