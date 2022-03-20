package com.example.campus.adaptar;

import static com.example.campus.view.RecyclerViewHelper.setViewText;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatFragmentContainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ChatContainAdapter";
    private final int mSize = 7;
    private List<String> dataList = new ArrayList<>(Arrays.asList("酒文化鉴赏与啤酒的酿造工艺讲解", "现代小说选集", "水利工程概论", "计算机概论", "医学解剖", "材料力学基础", "天体物理"));
    private List<String> dataListName = new ArrayList<>(Arrays.asList("刘墉", "方猪猪", "海盗狗", "海盗猪", "Aadsa", "Ddidae", "Mhala"));
    private List<String> dataListDate = new ArrayList<>(Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"));

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_fragment_contain_adpater, parent, false);
        Log.e(TAG, "onCreateViewHolder");
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setViewText(holder, R.id.chat_contain_name, dataListName.get(position));
        setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
        setViewText(holder, R.id.chat_contain_message, dataList.get(position));
        Log.e(TAG, "onBindViewHolder" + position);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            holder.itemView.setOnClickListener(v -> {
                dataList.set(position, "你在他乡还好吗？" + position);
                notifyItemChanged(position, 0);
                Log.e(TAG, "onCreateViewHolder  onClick " + position);
            });
            setViewText(holder, R.id.chat_contain_name, dataListName.get(position));
            setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
            setViewText(holder, R.id.chat_contain_message, dataList.get(position));
            Log.e(TAG, "onBindViewHolder init " + position);
        } else {
            int type = (int) payloads.get(0);
            updateItem(position, type, holder);
        }
    }


    @Override
    public int getItemCount() {
        return mSize;
    }

    private void updateItem(final int postion, int type, RecyclerView.ViewHolder holder) {
        switch (type) {
            //更新消息内容
            case 0:
                setViewText(holder, R.id.chat_contain_message, dataList.get(postion));
                break;
            //更新消息人名字
            case 1:
                setViewText(holder, R.id.chat_contain_name, dataListName.get(postion));
                break;
            //更新日期
            case 2:
                setViewText(holder, R.id.chat_contain_date, dataListDate.get(postion));
                break;
            default:
                break;
        }
    }


}
